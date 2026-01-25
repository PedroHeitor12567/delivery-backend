package com.pedroferreira.deliveryapplication.domain.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Store Domain Tests")
class StoreTest {

    private Store store;
    private City city;

    @BeforeEach
    void setUp() {
        city = City.builder()
                .id(1L)
                .name("São Paulo")
                .state("SP")
                .active(true)
                .build();

        store = Store.builder()
                .id(1L)
                .name("Test Store")
                .city(city)
                .phone("11988888888")
                .email("store@test.com")
                .address("Store Address")
                .category("Hamburgueria")
                .deliveryFeePerKm(BigDecimal.valueOf(2.50))
                .baseDeliveryFee(BigDecimal.valueOf(5.00))
                .minimumOrder(BigDecimal.valueOf(20.00))
                .openingTime(LocalTime.of(10, 0))
                .closingTime(LocalTime.of(22, 0))
                .active(true)
                .open(true)
                .build();
    }

    @Test
    @DisplayName("Deve calcular taxa de entrega corretamente")
    void shouldCalculateDeliveryFeeCorrectly() {
        BigDecimal fee1 = store.calculateDeliveryFee(5.0);
        assertThat(fee1).isEqualByComparingTo(BigDecimal.valueOf(17.50));

        BigDecimal fee2 = store.calculateDeliveryFee(1.0);
        assertThat(fee2).isEqualByComparingTo(BigDecimal.valueOf(7.50));

        BigDecimal fee3 = store.calculateDeliveryFee(10.0);
        assertThat(fee3).isEqualByComparingTo(BigDecimal.valueOf(30.00));
    }

    @Test
    @DisplayName("Deve lançar exceção para distância inválida")
    void shouldThrowExceptionForInvalidDistance() {
        assertThatThrownBy(() -> store.calculateDeliveryFee(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Distância inválida");

        assertThatThrownBy(() -> store.calculateDeliveryFee(0.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Distância inválida");

        assertThatThrownBy(() -> store.calculateDeliveryFee(-5.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Distância inválida");
    }

    @Test
    @DisplayName("Deve verificar se loja está aberta")
    void shouldCheckIfStoreIsOpen() {
        store.setOpeningTime(LocalTime.of(0, 0));
        store.setClosingTime(LocalTime.of(23, 59));

        boolean isOpen = store.isOpenNow();

        assertThat(isOpen).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false se cidade inativa")
    void shouldReturnFalseIfCityInactive() {
        city.deactivate();
        store.openStore(); // Tenta abrir mas cidade está inativa

        boolean isOpen = store.isOpenNow();

        assertThat(isOpen).isFalse();
    }

    @Test
    @DisplayName("Deve verificar se loja está fechada")
    void shouldReturnFalseIfStoreClosed() {
        store.openStore();
        store.closeStore();

        boolean isOpen = store.isOpenNow();

        assertThat(isOpen).isFalse();
    }

    @Test
    @DisplayName("Deve adicionar avaliação corretamente")
    void shouldAddRatingCorrectly() {
        store.addRating(5);
        store.addRating(4);
        store.addRating(5);

        assertThat(store.getRating()).isEqualByComparingTo(BigDecimal.valueOf(4.67));
        assertThat(store.getTotalRatings()).isEqualTo(3);
    }

    @Test
    @DisplayName("Deve lançar exceção para avaliação inválida")
    void shouldThrowExceptionForInvalidRating() {
        assertThatThrownBy(() -> store.addRating(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Avaliação deve ser entre 1 e 5");

        assertThatThrownBy(() -> store.addRating(6))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Avaliação deve ser entre 1 e 5");
    }

    @Test
    @DisplayName("Deve incrementar vendas")
    void shouldIncrementSales() {
        Integer initialSales = store.getTotalSales();

        store.incrementSales();
        store.incrementSales();

        assertThat(store.getTotalSales()).isEqualTo(initialSales + 2);
    }

    @Test
    @DisplayName("Deve abrir loja quando ativa e cidade ativa")
    void shouldOpenStoreWhenActiveAndCityActive() {
        store.closeStore();

        store.openStore();

        assertThat(store.getOpen()).isTrue();
    }

    @Test
    @DisplayName("Deve lançar exceção ao abrir loja desativada")
    void shouldThrowExceptionWhenOpeningDeactivatedStore() {
        store.deactivate();

        assertThatThrownBy(store::openStore)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Loja desativada não pode ser aberta");
    }

    @Test
    @DisplayName("Deve lançar exceção ao abrir loja em cidade inativa")
    void shouldThrowExceptionWhenOpeningStoreInInactiveCity() {
        city.deactivate();

        assertThatThrownBy(store::openStore)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Não é possível abrir loja em cidade inativa");
    }

    @Test
    @DisplayName("Deve fechar loja ao desativar")
    void shouldCloseStoreWhenDeactivating() {
        store.openStore();

        store.deactivate();

        assertThat(store.getActive()).isFalse();
        assertThat(store.getOpen()).isFalse();
    }

    @Test
    @DisplayName("Deve retornar nome e estado da cidade")
    void shouldReturnCityNameAndState() {
        assertThat(store.getCityName()).isEqualTo("São Paulo");
        assertThat(store.getState()).isEqualTo("SP");
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar loja sem cidade")
    void shouldThrowExceptionWhenCreatingStoreWithoutCity() {
        assertThatThrownBy(() -> new Store(
                "Test Store",
                null, // cidade nula
                "11988888888",
                "store@test.com",
                "Address",
                "Category",
                BigDecimal.valueOf(2.5),
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(20)
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cidade é obrigatória");
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar loja em cidade inativa")
    void shouldThrowExceptionWhenCreatingStoreInInactiveCity() {
        City inactiveCity = City.builder()
                .id(2L)
                .name("Rio de Janeiro")
                .state("RJ")
                .active(false)
                .build();

        assertThatThrownBy(() -> new Store(
                "Test Store",
                inactiveCity,
                "11988888888",
                "store@test.com",
                "Address",
                "Category",
                BigDecimal.valueOf(2.5),
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(20)
        ))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Não é possível criar loja em cidade inativa");
    }
}