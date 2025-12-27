package com.pedroferreira.deliveryapplication.domain.entity;

import com.pedroferreira.deliveryapplication.domain.enuns.EventRequest;
import com.pedroferreira.deliveryapplication.domain.enuns.StatusOrder;
import com.pedroferreira.deliveryapplication.domain.enuns.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemOrder> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusOrder status = StatusOrder.CREATED;

    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;

    @Column(name = "delivery_distance_km", precision = 5, scale = 2)
    private BigDecimal deliveryDistanceKm;

    @Column(name = "delivery_fee", precision = 10, scale = 2)
    BigDecimal deliveryFee;

    @Column(precision = 10, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "ready_at")
    private LocalDateTime readyAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @Column(name = "cacellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public void execute(EventRequest event, UserRole role) {
        StatusOrder newStatus = status.apply(event, role);
        this.status = newStatus;
        updateTimestamps(event);
    }

    private void updateTimestamps(EventRequest event) {
        switch (event) {
            case CONFIRM -> this.confirmedAt = LocalDateTime.now();
            case MARK_POINT -> this.readyAt = LocalDateTime.now();
            case DELIVER -> this.deliveredAt = LocalDateTime.now();
            case CANCEL, REFUSE -> this.canceledAt = LocalDateTime.now();
        }
    }

    public void confirm() {
        execute(EventRequest.CONFIRM, UserRole.SELLER);
    }

    public void refuse() {
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

    public void addItem(ItemOrder item) {
        items.add(item);
        item.setOrder(this);
        recalculateTotal();
    }

    public void recalculateTotal() {
        BigDecimal itemsTotal = items.stream()
                .map(ItemOrder::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalAmount = itemsTotal
                .add(deliveryFee != null ? deliveryFee : BigDecimal.ZERO)
                .subtract(discount);
    }

    public void setDeliveryDistance(Double distanceKm) {
        this.deliveryDistanceKm = BigDecimal.valueOf(distanceKm);
        this.deliveryFee = store.calculateDeliveryFee(distanceKm);
        recalculateTotal();
    }

    public void validate() {
        if (items.isEmpty()) {
            throw new IllegalStateException("Pedido deve conter pelo menos um item");
        }

        items.forEach(ItemOrder::validate);

        if (totalAmount.compareTo(store.getMinimumOrder()) < 0) {
            String.format("Valor mínimo do pedido é R$ %.2f", store.getMinimumOrder());
        }

        if (!store.isOpenNow()) {
            throw new IllegalStateException("Loja está fechada");
        }
    }

    public boolean canBeCanceled() {
        return status != StatusOrder.LEFT_FOR_DELIVERY
                && status != StatusOrder.DELIVERED
                && status != StatusOrder.CANCELED;
    }

    public boolean isFinal() {
        return status.ehFinal();
    }
}
