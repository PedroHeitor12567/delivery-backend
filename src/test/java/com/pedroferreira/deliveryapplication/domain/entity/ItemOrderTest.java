package com.pedroferreira.deliveryapplication.domain.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ItemOrder Domain Tests")
public class ItemOrderTest {

    private Product product;
    private Store store;

    @BeforeEach
    public void setUp() {
        store = Store.builder()
                .id(1L)
                .name("Test Store")
                .city("Riacho de Santana")
                .state("RN")
                .phone("11988888888")
                .email("store@test.com")
                .address("Store Address")
                .category("Doceria")
                .deliveryFeePerKm(BigDecimal.valueOf(2.5))
                .baseDeliveryFee(BigDecimal.valueOf(5))
                .minimumOrder(BigDecimal.valueOf(20))
                .active(true)
                .open(true)
                .build();

        product = Product.builder()
                .id(1L)
                .name("Torta de limão")
                .price(BigDecimal.valueOf(30))
                .available(true)
                .active(true)
                .store(store)
                .build();
    }

    @Test
    @DisplayName("Deve criar item de pedido com sucesso")
    void shouldCreateItemOrderSuccessfully() {
        ItemOrder item = new ItemOrder(product, 2, product.getPrice(), "Pedaço grande");

        assertThat(item).isNotNull();
        assertThat(item.getProduct()).isEqualTo(product);
        assertThat(item.getQuantity()).isEqualTo(2);
        assertThat(item.getUnitPrice()).isEqualByComparingTo(BigDecimal.valueOf(30.0));
        assertThat(item.getObservations()).isEqualTo("Pedaço grande");
        assertThat(item.getDiscount()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Deve calcular subtotal corretamente")
    void shouldCalculateSubtotalCorrectly() {
        ItemOrder item1 = new ItemOrder(product, 2, product.getPrice(), null);
        assertThat(item1.getSubtotal()).isEqualByComparingTo(BigDecimal.valueOf(60.0));

        ItemOrder item2 = new ItemOrder(product, 3, product.getPrice(), null);
        item2.setDiscount(BigDecimal.valueOf(10.0));
        assertThat(item2.getSubtotal()).isEqualByComparingTo(BigDecimal.valueOf(80.0));
    }

    @Test
    @DisplayName("Deve validar item corretamente")
    void shouldValidateItemCorrectly() {
        ItemOrder item = new ItemOrder(product, 2, product.getPrice(), null);

        assertThatCode(item::validate).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar item com quantidade inválida")
    void shouldThrowExceptionWhenValiditingItemWithInvalidQuantity() {
        ItemOrder item = ItemOrder.builder()
                .product(product)
                .quantity(0)
                .unitPrice(product.getPrice())
                .build();

        assertThatThrownBy(item::validate)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Quantidade deve ser maior que zero");
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar item com preço inválido")
    void shouldThrowExceptionWhenValiditingItemWithInvalidPrice() {
        ItemOrder item = ItemOrder.builder()
                .product(product)
                .quantity(1)
                .unitPrice(BigDecimal.ZERO)
                .build();

        assertThatThrownBy(item::validate)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço unitário deve ser maior que zero");
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar item sem produto")
    void shouldThrowExceptionWhenValidatingItemWithoutProduct() {
        ItemOrder item = ItemOrder.builder()
                .product(null)
                .quantity(1)
                .unitPrice(BigDecimal.valueOf(10.0))
                .build();

        assertThatThrownBy(item::validate)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Produto não pode ser nulo");
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar item com produto indisponível")
    void shouldThrowExceptionWhenValidatingItemWithUnavailableProduct() {
        product.makeUnavailable();
        ItemOrder item = new ItemOrder(product, 1, product.getPrice(), null);

        assertThatThrownBy(item::validate)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Produto não está disponível");
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar item sem produto")
    void shouldThrowExceptionWhenCreatingItemWithoutProduct() {
        assertThatThrownBy(() -> new ItemOrder(null, 1, BigDecimal.valueOf(10.0), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Produto não pode ser nulo");
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar item com quantidade inválida")
    void shouldThrowExceptionWhenCreatingItemWithInvalidQuantity() {
        assertThatThrownBy(() -> new ItemOrder(product, 0, product.getPrice(), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Quantidade deve ser maior que zero");

        assertThatThrownBy(() -> new ItemOrder(product, -1, product.getPrice(), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Quantidade deve ser maior que zero");
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar item com preço inválido")
    void shouldThrowExceptionWhenCreatingItemWithInvalidPrice() {
        assertThatThrownBy(() -> new ItemOrder(product, 1, BigDecimal.ZERO, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço unitário deve ser maior do que zero");

        assertThatThrownBy(() -> new ItemOrder(product, 1, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço unitário deve ser maior do que zero");
    }
}
