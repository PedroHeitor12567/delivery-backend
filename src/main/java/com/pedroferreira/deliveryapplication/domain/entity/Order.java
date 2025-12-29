package com.pedroferreira.deliveryapplication.domain.entity;

import com.pedroferreira.deliveryapplication.domain.enuns.EventRequest;
import com.pedroferreira.deliveryapplication.domain.enuns.StatusOrder;
import com.pedroferreira.deliveryapplication.domain.enuns.UserRole;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"customer", "store", "items"})
public class Order {

    @EqualsAndHashCode.Include
    private Long id;
    private Customer customer;
    private Store store;

    @Builder.Default
    private List<ItemOrder> items = new ArrayList<>();

    @Builder.Default
    private StatusOrder status = StatusOrder.CREATED;

    private String deliveryAddress;
    private BigDecimal deliveryDistanceKm;
    private BigDecimal deliveryFee;

    @Builder.Default
    private BigDecimal discount = BigDecimal.ZERO;

    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime readyAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime canceledAt;
    private String observations;
    private String cancellationReason;

    public Order(Customer customer, Store store, String deliveryAddress,
                 Double deliveryDistanceKm, String observations) {
        validateConstructorParams(customer, store, deliveryAddress, deliveryDistanceKm);

        this.customer = customer;
        this.store = store;
        this.deliveryAddress = deliveryAddress;
        this.deliveryDistanceKm = BigDecimal.valueOf(deliveryDistanceKm);
        this.observations = observations;
        this.status = StatusOrder.CREATED;
        this.createdAt = LocalDateTime.now();
        this.items = new ArrayList<>();
        this.discount = BigDecimal.ZERO;
        this.deliveryFee = store.calculateDeliveryFee(deliveryDistanceKm);
    }

    public void execute(EventRequest event, UserRole role) {
        StatusOrder newStatus = status.apply(event, role);
        this.status = newStatus;
        updateTimestamps(event);
    }

    public void addItem(ItemOrder item) {
        if (item == null) {
            throw new IllegalArgumentException("Item não pode ser nulo");
        }
        items.add(item);
        item.setOrder(this);
        recalculateTotal();
    }

    public void recalculateTotal() {
        BigDecimal itemsTotal = items.stream()
                .map(ItemOrder::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal deliveryAmount = deliveryFee != null ? deliveryFee : BigDecimal.ZERO;
        BigDecimal discountAmount = discount != null ? discount : BigDecimal.ZERO;

        this.totalAmount = itemsTotal.add(deliveryAmount).subtract(discountAmount);
    }

    public void validate() {
        if (items.isEmpty()) {
            throw new IllegalStateException("Pedido deve conter ao menos um item");
        }

        items.forEach(ItemOrder::validate);

        if (totalAmount.compareTo(store.getMinimumOrder()) < 0) {
            throw new IllegalStateException(
                    String.format("Valor mínimo do pedido é R$ %.2f", store.getMinimumOrder())
            );
        }

        if (!store.isOpenNow()) {
            throw new IllegalStateException("Loja está fechada");
        }
    }

    // Métodos auxiliares de transição
    public void confirm() {
        execute(EventRequest.CONFIRM, UserRole.SELLER);
    }

    public void refuse(String reason) {
        this.cancellationReason = reason;
        execute(EventRequest.REFUSE, UserRole.SELLER);
    }

    public void markReady() {
        execute(EventRequest.MARK_POINT, UserRole.SELLER);
    }

    public void exitForDelivery() {
        execute(EventRequest.EXIT_FOR_DELIVERY, UserRole.SYSTEM);
    }

    public void deliver() {
        execute(EventRequest.DELIVER, UserRole.SYSTEM);
    }

    public void cancel(String reason) {
        this.cancellationReason = reason;
        execute(EventRequest.CANCEL, UserRole.CUSTOMER);
    }

    public boolean canBeCanceled() {
        return status != StatusOrder.LEFT_FOR_DELIVERY
                && status != StatusOrder.DELIVERED
                && status != StatusOrder.CANCELED;
    }

    public boolean isFinal() {
        return status.ehFinal();
    }

    // ==================== MÉTODOS PRIVADOS ====================

    private void updateTimestamps(EventRequest event) {
        LocalDateTime now = LocalDateTime.now();
        switch (event) {
            case CONFIRM -> this.confirmedAt = now;
            case MARK_POINT -> this.readyAt = now;
            case DELIVER -> this.deliveredAt = now;
            case CANCEL, REFUSE -> this.canceledAt = now;
            default -> {}
        }
    }

    private void validateConstructorParams(Customer customer, Store store,
                                           String deliveryAddress, Double distanceKm) {
        if (customer == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo");
        }
        if (store == null) {
            throw new IllegalArgumentException("Loja não pode ser nula");
        }
        if (deliveryAddress == null || deliveryAddress.isBlank()) {
            throw new IllegalArgumentException("Endereço de entrega é obrigatório");
        }
        if (distanceKm == null || distanceKm <= 0) {
            throw new IllegalArgumentException("Distância deve ser maior que zero");
        }
    }
}