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

    @BeforeEach
    void setUp() {
        store = Store.builder()
                .id(1L)
                .name("Test Store")
                .city("São Paulo")
                .state("SP")
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
        // Test Case 1: 5km
        BigDecimal fee1 = store.calculateDeliveryFee(5.0);
        assertThat(fee1).isEqualByComparingTo(BigDecimal.valueOf(17.50)); // 5 + (5*2.5)

        // Test Case 2: 1km
        BigDecimal fee2 = store.calculateDeliveryFee(1.0);
        assertThat(fee2).isEqualByComparingTo(BigDecimal.valueOf(7.50)); // 5 + (1*2.5)

        // Test Case 3: 10km
        BigDecimal fee3 = store.calculateDeliveryFee(10.0);
        assertThat(fee3).isEqualByComparingTo(BigDecimal.valueOf(30.00)); // 5 + (10*2.5)
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
        // Given - horário de 00:00 às 23:59
        store.setOpeningTime(LocalTime.of(0, 0));
        store.setClosingTime(LocalTime.of(23, 59));

        // When
        boolean isOpen = store.isOpenNow();

        // Then
        assertThat(isOpen).isTrue();
    }

    @Test
    @DisplayName("Deve verificar se loja está fechada")
    void shouldReturnFalseIfStoreClosed() {
        // Given
        store.openStore();  // Primeiro abre
        store.closeStore(); // Depois fecha

        // When
        boolean isOpen = store.isOpenNow();

        // Then
        assertThat(isOpen).isFalse();
    }

    @Test
    @DisplayName("Deve adicionar avaliação corretamente")
    void shouldAddRatingCorrectly() {
        // Given & When
        store.addRating(5);
        store.addRating(4);
        store.addRating(5);

        // Then
        // Média: (5 + 4 + 5) / 3 = 4.67
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
        // Given
        Integer initialSales = store.getTotalSales();

        // When
        store.incrementSales();
        store.incrementSales();

        // Then
        assertThat(store.getTotalSales()).isEqualTo(initialSales + 2);
    }

    @Test
    @DisplayName("Deve abrir loja quando ativa")
    void shouldOpenStoreWhenActive() {
        // Given
        store.closeStore();

        // When
        store.openStore();

        // Then
        assertThat(store.getOpen()).isTrue();
    }

    @Test
    @DisplayName("Deve lançar exceção ao abrir loja desativada")
    void shouldThrowExceptionWhenOpeningDeactivatedStore() {
        // Given
        store.deactivate();

        // When & Then
        assertThatThrownBy(store::openStore)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Loja desativada não pode ser aberta");
    }

    @Test
    @DisplayName("Deve fechar loja ao desativar")
    void shouldCloseStoreWhenDeactivating() {
        // Given
        store.openStore();

        // When
        store.deactivate();

        // Then
        assertThat(store.getActive()).isFalse();
        assertThat(store.getOpen()).isFalse();
    }
}
