package com.pedroferreira.deliveryapplication.infrastructure.persistence.entity;

import com.pedroferreira.deliveryapplication.domain.entity.Customer;
import com.pedroferreira.deliveryapplication.domain.entity.ItemOrder;
import com.pedroferreira.deliveryapplication.domain.entity.Order;
import com.pedroferreira.deliveryapplication.domain.entity.Store;
import com.pedroferreira.deliveryapplication.domain.enuns.StatusOrder;
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
public class OrderJpaEntity {

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
}

public Order toDomain(CustomerJpaEntity customerJpa, StoreJpaEntity storeJpa) {
    Order order = new Order();
    order.setId(this.id);
    order.setCustomer(customerJpa.toDomain());
}
