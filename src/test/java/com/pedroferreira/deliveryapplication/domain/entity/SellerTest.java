package com.pedroferreira.deliveryapplication.domain.entity;

import com.pedroferreira.deliveryapplication.domain.enuns.StatusOrder;
import com.pedroferreira.deliveryapplication.domain.enuns.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Seller Domain Tests")
class SellerTest {

    private Seller seller;
    private Store store;
    private Customer customer;
    private Order order;

    @BeforeEach
    void setUp() {
        store = Store.builder()
                .id(1L)
                .name("Test Store")
                .city(City.builder().build())
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

        seller = Seller.builder()
                .id(1L)
                .username("userseller")
                .email("seller@test.com")
                .password("password")
                .cpf("98765432100")
                .phone("11988887777")
                .address("Seller Address")
                .store(store)
                .build();

        customer = Customer.builder()
                .id(1L)
                .username("customer1")
                .email("customer@test.com")
                .password("password")
                .cpf("12345678901")
                .phone("11999999999")
                .address("Customer Address")
                .build();

        Product product = Product.builder()
                .id(1L)
                .name("X-Burger")
                .price(BigDecimal.valueOf(30.00))
                .available(true)
                .active(true)
                .store(store)
                .build();

        order = new Order(customer, store, "Delivery Address", 5.0, null);
        ItemOrder item = new ItemOrder(product, 2, product.getPrice(), null);
        order.addItem(item);
    }

    @Test
    @DisplayName("Deve criar vendedor com sucesso")
    void shouldCreateSellerSuccessfully() {
        assertThat(seller).isNotNull();
        assertThat(seller.getUsername()).isEqualTo("userseller");
        assertThat(seller.getEmail()).isEqualTo("seller@test.com");
        assertThat(seller.getStore()).isEqualTo(store);
        assertThat(seller.isActive()).isTrue();
        assertThat(seller.getUserRole()).isEqualTo(UserRole.SELLER);
    }

    @Test
    @DisplayName("Vendedor deve aceitar pedido da sua loja")
    void shouldAcceptOrderFromOwnStore() {
        seller.acceptOrder(order);

        assertThat(order.getStatus())
                .isEqualTo(StatusOrder.CONFIRMED);
        assertThat(order.getConfirmedAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve lançar exceção ao aceitar pedido de outra loja")
    void shouldThrowExceptionWhenAcceptingOrderFromDifferentStore() {
        Store otherStore = Store.builder()
                .id(2L)
                .name("Other Store")
                .city(City.builder().build())
                .phone("21988888888")
                .email("other@test.com")
                .address("Other Address")
                .category("Pizzaria")
                .deliveryFeePerKm(BigDecimal.valueOf(3.00))
                .baseDeliveryFee(BigDecimal.valueOf(6.00))
                .minimumOrder(BigDecimal.valueOf(25.00))
                .active(true)
                .open(true)
                .build();

        Order orderFromOtherStore = new Order(customer, otherStore, "Delivery Address", 5.0, null);

        assertThatThrownBy(() -> seller.acceptOrder(orderFromOtherStore))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Pedido não pertence a esta loja");
    }

    @Test
    @DisplayName("Vendedor deve recusar pedido com motivo")
    void shouldRefuseOrderWithReason() {
        // When
        seller.refuseOrder(order, "Ingredientes em falta");

        // Then
        assertThat(order.getStatus())
                .isEqualTo(com.pedroferreira.deliveryapplication.domain.enuns.StatusOrder.CANCELED);
        assertThat(order.getCancellationReason()).isEqualTo("Ingredientes em falta");
        assertThat(order.getCanceledAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve lançar exceção ao recusar pedido de outra loja")
    void shouldThrowExceptionWhenRefusingOrderFromDifferentStore() {
        Store otherStore = Store.builder()
                .id(2L)
                .name("Other Store")
                .city(City.builder().build())
                .phone("21988888888")
                .email("other@test.com")
                .address("Other Address")
                .category("Pizzaria")
                .deliveryFeePerKm(BigDecimal.valueOf(3.00))
                .baseDeliveryFee(BigDecimal.valueOf(6.00))
                .minimumOrder(BigDecimal.valueOf(25.00))
                .active(true)
                .open(true)
                .build();

        Order orderFromOtherStore = new Order(customer, otherStore, "Delivery Address", 5.0, null);

        assertThatThrownBy(() -> seller.refuseOrder(orderFromOtherStore, "Motivo"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Pedido não pertence a esta loja");
    }

    @Test
    @DisplayName("Vendedor deve marcar pedido como pronto")
    void shouldMarkOrderAsReady() {
        seller.acceptOrder(order);

        seller.markOrderReady(order);

        assertThat(order.getStatus()).isEqualTo(StatusOrder.READY);
        assertThat(order.getReadyAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve lançar exceção ao marcar pedido de outra loja como pronto")
    void shouldThrowExceptionWhenMarkingOrderFromDifferentStoreAsReady() {
        Store otherStore = Store.builder()
                .id(2L)
                .name("Other Store")
                .city(City.builder().build())
                .phone("21988888888")
                .email("other@test.com")
                .address("Other Address")
                .category("Pizzaria")
                .deliveryFeePerKm(BigDecimal.valueOf(3.00))
                .baseDeliveryFee(BigDecimal.valueOf(6.00))
                .minimumOrder(BigDecimal.valueOf(25.00))
                .active(true)
                .open(true)
                .build();

        Order orderFromOtherStore = new Order(customer, otherStore, "Delivery Address", 5.0, null);

        assertThatThrownBy(() -> seller.markOrderReady(orderFromOtherStore))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Pedido não pertence a esta loja");
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar pedido sem loja associada ao vendedor")
    void shouldThrowExceptionWhenSellerHasStore() {
        Seller sellerWithoutStore = Seller.builder()
                .id(2L)
                .username("seller2")
                .email("seller2@test.com")
                .password("password")
                .cpf("11122233344")
                .phone("11977777777")
                .address("Address")
                .store(null)
                .build();

        assertThatThrownBy(() -> sellerWithoutStore.acceptOrder(order))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Pedido não pertence a esta loja");
    }

    @Test
    @DisplayName("Deve desabilitar e habilitar vendedor")
    void shouldDisableAndEnableSeller() {
        seller.disable();

        assertThat(seller.isActive()).isFalse();

        seller.enable();

        assertThat(seller.isActive()).isTrue();
    }
}
