package com.pedroferreira.deliveryapplication.domain.entity;

import com.pedroferreira.deliveryapplication.domain.enuns.StatusOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes de Domain - SEM frameworks, apenas regras de negócio
 */
@DisplayName("Order Domain Tests")
class OrderTest {

    private Customer customer;
    private Store store;
    private Product product;

    @BeforeEach
    void setUp() {
        // Setup Customer
        customer = Customer.builder()
                .id(1L)
                .username("testuser")
                .email("test@email.com")
                .password("password")
                .cpf("12345678901")
                .phone("11999999999")
                .address("Test Address, 123")
                .build();

        // Setup Store
        store = Store.builder()
                .id(1L)
                .name("Test Store")
                .city("São Paulo")
                .state("SP")
                .phone("11988888888")
                .email("store@test.com")
                .address("Store Address, 456")
                .category("Hamburgueria")
                .deliveryFeePerKm(BigDecimal.valueOf(2.50))
                .baseDeliveryFee(BigDecimal.valueOf(5.00))
                .minimumOrder(BigDecimal.valueOf(20.00))
                .openingTime(LocalTime.of(0, 0))
                .closingTime(LocalTime.of(23, 59))
                .active(true)
                .open(true)
                .build();

        // Setup Product
        product = Product.builder()
                .id(1L)
                .name("X-Burger")
                .price(BigDecimal.valueOf(30.00))
                .available(true)
                .active(true)
                .store(store)
                .build();
    }

    @Test
    @DisplayName("Deve criar pedido com sucesso")
    void shouldCreateOrderSuccessfully() {
        // Given & When
        Order order = new Order(customer, store, "Delivery Address", 5.0, "Observations");

        ItemOrder item = new ItemOrder(product, 2, product.getPrice(), null);
        order.addItem(item);

        // Then
        assertThat(order.getStatus()).isEqualTo(StatusOrder.CREATED);
        assertThat(order.getDeliveryFee()).isEqualByComparingTo(BigDecimal.valueOf(17.50)); // 5 + (5*2.5)
        assertThat(order.getTotalAmount()).isEqualByComparingTo(BigDecimal.valueOf(77.50)); // (30*2) + 17.5
        assertThat(order.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar pedido com distância inválida")
    void shouldThrowExceptionWhenInvalidDistance() {
        // When & Then
        assertThatThrownBy(() -> new Order(customer, store, "Address", -1.0, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Distância deve ser maior que zero");
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar pedido sem endereço")
    void shouldThrowExceptionWhenNoAddress() {
        // When & Then
        assertThatThrownBy(() -> new Order(customer, store, "", 5.0, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Endereço de entrega é obrigatório");
    }

    @Test
    @DisplayName("Deve confirmar pedido corretamente")
    void shouldConfirmOrderCorrectly() {
        // Given
        Order order = new Order(customer, store, "Address", 5.0, null);

        // When
        order.confirm();

        // Then
        assertThat(order.getStatus()).isEqualTo(StatusOrder.CONFIRMED);
        assertThat(order.getConfirmedAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve marcar pedido como pronto")
    void shouldMarkOrderAsReady() {
        // Given
        Order order = new Order(customer, store, "Address", 5.0, null);
        order.confirm();

        // When
        order.markReady();

        // Then
        assertThat(order.getStatus()).isEqualTo(StatusOrder.READY);
        assertThat(order.getReadyAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve validar pedido com valor abaixo do mínimo")
    void shouldValidateMinimumOrderValue() {
        // Given
        store.setMinimumOrder(BigDecimal.valueOf(100.00));
        Order order = new Order(customer, store, "Address", 5.0, null);

        Product cheapProduct = Product.builder()
                .id(2L)
                .name("Cheap Product")
                .price(BigDecimal.valueOf(10.00))
                .available(true)
                .store(store)
                .build();

        ItemOrder item = new ItemOrder(cheapProduct, 1, cheapProduct.getPrice(), null);
        order.addItem(item);

        // When & Then
        assertThatThrownBy(order::validate)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Valor mínimo do pedido é R$ 100,00");
    }

    @Test
    @DisplayName("Deve recalcular total ao adicionar itens")
    void shouldRecalculateTotalWhenAddingItems() {
        // Given
        Order order = new Order(customer, store, "Address", 5.0, null);

        ItemOrder item1 = new ItemOrder(product, 2, product.getPrice(), null);
        ItemOrder item2 = new ItemOrder(product, 1, product.getPrice(), null);

        // When
        order.addItem(item1);
        order.addItem(item2);

        // Then
        // (30 * 2) + (30 * 1) + 17.5 = 107.50
        assertThat(order.getTotalAmount()).isEqualByComparingTo(BigDecimal.valueOf(107.50));
    }

    @Test
    @DisplayName("Deve cancelar pedido quando permitido")
    void shouldCancelOrderWhenAllowed() {
        // Given
        Order order = new Order(customer, store, "Address", 5.0, null);

        // When
        order.cancel("Mudei de ideia");

        // Then
        assertThat(order.getStatus()).isEqualTo(StatusOrder.CANCELED);
        assertThat(order.getCancellationReason()).isEqualTo("Mudei de ideia");
        assertThat(order.getCanceledAt()).isNotNull();
    }

    @Test
    @DisplayName("Não deve permitir cancelamento após saída para entrega")
    void shouldNotAllowCancelAfterLeftForDelivery() {
        // Given
        Order order = new Order(customer, store, "Address", 5.0, null);
        order.confirm();
        order.markReady();
        order.exitForDelivery();

        // When & Then
        assertThat(order.canBeCanceled()).isFalse();
    }
}
