package com.pedroferreira.deliveryapplication.domain.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Customer Domain Test")
class CustomerTest {

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .username("testusername")
                .email("test@email.com")
                .password("test123")
                .cpf("12345678901")
                .phone("11999999999")
                .address("Test Address, 123")
                .build();
    }

    @Test
    @DisplayName("Deve criar cliente com sucesso")
    void shouldCreateCustomerSuccessfully() {
        assertThat(customer).isNotNull();
        assertThat(customer.getUsername()).isEqualTo("testusername");
        assertThat(customer.getEmail()).isEqualTo("test@email.com");
        assertThat(customer.getLoyaltyPoints()).isEqualTo(0);
        assertThat(customer.isActive()).isTrue();
        assertThat(customer.getOrders()).isEmpty();
    }

    @Test
    @DisplayName("Deve adicionar pontos de fidelidade")
    void shouldAddLoyaltyPoints() {

        customer.addLoyaltyPoints(100);
        customer.addLoyaltyPoints(50);

        assertThat(customer.getLoyaltyPoints()).isEqualTo(150);
    }

    @Test
    @DisplayName("Deve lançar exceção ao adicionar pontos negativos ou zero")
    void shouldThrowExceptionWhenAddingInvalidPoints() {
        assertThatThrownBy(() -> customer.addLoyaltyPoints(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Pontos devem ser positivos");

        assertThatThrownBy(() -> customer.addLoyaltyPoints(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Pontos devem ser positivos");

        assertThatThrownBy(() -> customer.addLoyaltyPoints(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Pontos devem ser positivos");
    }

    @Test
    @DisplayName("Deve usar pontos de fidelidade")
    void shouldUseLoyaltyPoints() {
        customer.addLoyaltyPoints(100);

        customer.useLoyaltyPoints(50);

        assertThat(customer.getLoyaltyPoints()).isEqualTo(50);
    }

    @Test
    @DisplayName("Deve lançar exceção ao usar mais pontos do que possui")
    void shouldThrowExceptionWhenUsingMorePointsThanAvailable() {
        customer.addLoyaltyPoints(50);

        assertThatThrownBy(() -> customer.useLoyaltyPoints(100))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Pontos de fidelidade insuficientes");
    }

    @Test
    @DisplayName("Deve adicionar pedido ao cliente")
    void shouldAddOrderToCustomer() {
        Store store = Store.builder()
                .id(1L)
                .name("Test Store")
                .city("Pau dos Ferros")
                .state("RN")
                .phone("11988888888")
                .email("store@test.com")
                .address("Store Address")
                .category("Hamburgueria")
                .deliveryFeePerKm(BigDecimal.valueOf(2.5))
                .baseDeliveryFee(BigDecimal.valueOf(5))
                .minimumOrder(BigDecimal.valueOf(20))
                .active(true)
                .open(true)
                .build();

        Order order = new Order(customer, store, "Delivery Address", 5.0, null);

        customer.addOrder(order);

        assertThat(customer.getOrders()).hasSize(1);
        assertThat(customer.getOrders()).contains(order);
    }

    @Test
    @DisplayName("Deve lançar exceção ao adicionar pedido nulo")
    void shouldThrowExceptionWhenAddingNullOrder() {
        assertThatThrownBy(() -> customer.addOrder(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Pedido não pode ser nulo");
    }

    @Test
    @DisplayName("Deve desabilitar e habilitar cliente")
    void shouldDisableAndEnableCustomer() {
        customer.disable();
        assertThat(customer.isActive()).isFalse();

        customer.enable();
        assertThat(customer.isActive()).isTrue();
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar cliente sem dados obrigatórios")
    void shouldThrowExceptionWhenCreatingCustomerWithoutRequiredData() {
        assertThatThrownBy(() -> Customer.builder()
                .username("")
                .email("test@email.com")
                .password("test123")
                .cpf("12345678901")
                .phone("11999999999")
                .address("Test Address, 123")
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username é obrigatório");

        assertThatThrownBy(() -> Customer.builder()
        .username("username")
                .email("")
                .password("pass")
                .cpf("12345678900")
                .phone("11999999999")
                .address("Test Address, 123")
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email é obrigatório");
    }
}
