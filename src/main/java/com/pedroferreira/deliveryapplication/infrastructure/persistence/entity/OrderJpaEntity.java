package com.pedroferreira.deliveryapplication.infrastructure.persistence.entity;

import com.pedroferreira.deliveryapplication.domain.entity.ItemOrder;
import com.pedroferreira.deliveryapplication.domain.entity.Order;
import com.pedroferreira.deliveryapplication.domain.enuns.StatusOrder;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerJpaEntity customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreJpaEntity store;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemOrderJpaEntity> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private StatusOrder status;

    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;

    @Column(name = "delivery_distance_km", precision = 5, scale = 2)
    private BigDecimal deliveryDistanceKm;

    @Column(name = "delivery_fee", precision = 10, scale = 2)
    BigDecimal deliveryFee;

    @Column(precision = 10, scale = 2)
    private BigDecimal discount;

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

    public static OrderJpaEntity fromDomain(Order order) {
        if (order == null) return null;

        OrderJpaEntity jpaEntity = OrderJpaEntity.builder()
                .id(order.getId())
                .status(order.getStatus())
                .deliveryAddress(order.getDeliveryAddress())
                .deliveryDistanceKm(order.getDeliveryDistanceKm())
                .deliveryFee(order.getDeliveryFee())
                .discount(order.getDiscount())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .confirmedAt(order.getConfirmedAt())
                .readyAt(order.getReadyAt())
                .deliveredAt(order.getDeliveredAt())
                .canceledAt(order.getCanceledAt())
                .observations(order.getObservations())
                .cancellationReason(order.getCancellationReason())
                .build();

        if (order.getItems() != null) {
            List<ItemOrderJpaEntity> jpaItems = order.getItems().stream()
                    .map(item -> {
                        ItemOrderJpaEntity jpaItem = ItemOrderJpaEntity.fromDomain(item);
                        jpaItem.setOrder(jpaEntity);
                        return jpaItem;
                    })
                    .collect(Collectors.toList());
            jpaEntity.setItems(jpaItems);
        }

        return jpaEntity;
    }

    public Order toDomain() {
        Order order = Order.builder()
                .id(this.id)
                .status(this.status)
                .deliveryAddress(this.deliveryAddress)
                .deliveryDistanceKm(this.deliveryDistanceKm)
                .deliveryFee(this.deliveryFee)
                .discount(this.discount)
                .totalAmount(this.totalAmount)
                .createdAt(this.createdAt)
                .observations(this.observations)
                .cancellationReason(this.cancellationReason)
                .build();

        order.setConfirmedAt(this.confirmedAt);
        order.setReadyAt(this.readyAt);
        order.setDeliveredAt(this.deliveredAt);
        order.setCanceledAt(this.canceledAt);

        if (this.customer != null) {
            order.setCustomer(this.customer.toDomain());
        }

        if (this.store != null) {
            order.setStore(this.store.toDomain());
        }

        if (this.items != null && !items.isEmpty()) {
            List<ItemOrder> domainItems = this.items.stream()
                    .map(ItemOrderJpaEntity::toDomain)
                    .collect(Collectors.toList());
            order.setItems(domainItems);
        }

        return order;
    }

}



