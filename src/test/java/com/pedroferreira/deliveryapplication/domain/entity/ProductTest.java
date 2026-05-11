package com.pedroferreira.deliveryapplication.domain.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

@DisplayName("Product Domain Tests")
class ProductTest {

    private Store store;
    private Product product;

    @BeforeEach
    void setUp() {
        store = Store.builder()
                .id(1L)
                .name("Test Store")
                .city(City.builder().build())
                .phone("11988888888")
                .email("store@test.com")
                .address("Store Address")
                .category("Pizzaria")
                .deliveryFeePerKm(BigDecimal.valueOf(2.5))
                .baseDeliveryFee(BigDecimal.valueOf(5))
                .minimumOrder(BigDecimal.valueOf(20))
                .active(true)
                .open(true)
                .build();

        product = Product.builder()
                .id(1L)
                .name("Calabresa")
                .description("Acompanha companhamentos")
                .price(BigDecimal.valueOf(35))
                .imageUrl("http://example.com/burger.jpg")
                .store(store)
                .available(true)
                .active(true)
                .preparationTime(30)
                .build();
    }

    @Test
    @DisplayName("Deve criar produto com sucesso")
    void shouldCreateProductSuccessfully() {
        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo("Calabresa");
        assertThat(product.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(35));
        assertThat(product.getAvailable()).isTrue();
        assertThat(product.getActive()).isTrue();
        assertThat(product.getStore()).isEqualTo(store);
    }

    @Test
    @DisplayName("Deve criar produto usando construtor simplificado")
    void shouldCreateProductUsingSimpleConstructor() {
        Product simpleProduct = new Product("Simple Product", BigDecimal.valueOf(20.0), store);

        assertThat(simpleProduct.getName()).isEqualTo("Simple Product");
        assertThat(simpleProduct.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(20.0));
        assertThat(simpleProduct.getStore()).isEqualTo(store);
        assertThat(simpleProduct.getAvailable()).isTrue();
        assertThat(simpleProduct.getActive()).isTrue();
    }

    @Test
    @DisplayName("Deve ativar produto")
    void shouldActivateProduct() {
        product.deactivate();

        product.activate();

        assertThat(product.getActive()).isTrue();
        assertThat(product.getAvailable()).isTrue();
    }

    @Test
    @DisplayName("Deve desativar produto")
    void shouldDeactivateProduct() {
        product.deactivate();

        assertThat(product.getActive()).isFalse();
        assertThat(product.getAvailable()).isFalse();
    }

    @Test
    @DisplayName("Deve tornar produto indisponível")
    void shouldMakeProductUnavailable() {
        product.makeUnavailable();

        assertThat(product.getAvailable()).isFalse();
        assertThat(product.getActive()).isTrue();
    }

    @Test
    @DisplayName("Deve tornar produto disponível quando ativo")
    void shouldMakeProductAvailableWhenActive() {
        product.makeUnavailable();

        product.makeAvailable();

        assertThat(product.getAvailable()).isTrue();
    }

    @Test
    @DisplayName("Não deve tornar produto disponível quando inativo")
    void shouldNotMakeProductAvailableWhenInactive() {
        product.deactivate();

        product.makeAvailable();

        assertThat(product.getAvailable()).isFalse();
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar produto sem nome")
    void shouldThrowExceptionWhenCreatingProductWithoutName() {
        assertThatThrownBy(() -> new Product("", BigDecimal.valueOf(10.0), store))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nome do produto é obrigatório");

        assertThatThrownBy(() -> new Product(null, BigDecimal.valueOf(10.0), store))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nome do produto é obrigatório");
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar produto com preço inválido")
    void shouldThrowExceptionWhenCreatingProductWithInvalidPrice() {
        assertThatThrownBy(() -> new Product("Product", BigDecimal.ZERO, store))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço deve ser maior que zero");

        assertThatThrownBy(() -> new Product("Product", BigDecimal.valueOf(-10), store))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço deve ser maior que zero");

        assertThatThrownBy(() -> new Product("Product", null, store))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço deve ser maior que zero");
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar produto sem loja")
    void shouldThrowExceptionWhenCreatingProductWithoutStore() {
        assertThatThrownBy(() -> new Product("Product", BigDecimal.valueOf(10.0), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Loja não pode ser nula");

    }
}
