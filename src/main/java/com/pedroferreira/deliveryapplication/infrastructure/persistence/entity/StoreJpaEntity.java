package com.pedroferreira.deliveryapplication.infrastructure.persistence.entity;

import com.pedroferreira.deliveryapplication.domain.entity.Admin;
import com.pedroferreira.deliveryapplication.domain.entity.Order;
import com.pedroferreira.deliveryapplication.domain.entity.Product;
import com.pedroferreira.deliveryapplication.domain.entity.Store;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Table(name = "stores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @OneToMany(mappedBy = "store")
    private List<ProductJpaEntity> products = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<OrderJpaEntity> orders = new ArrayList<>();

    @Column(name = "total_sales")
    private Integer totalSales = 0;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String category;

    @Column(name = "opening_time")
    private LocalTime openingTime;

    @Column(name = "closing_time")
    private LocalTime closingTime;

    @Column(name = "delivery_fee_per_km", precision = 10, scale = 2)
    private BigDecimal deliveryFeePerKm;

    @Column(name = "base_delivery_fee", precision = 10, scale = 2)
    private BigDecimal baseDeliveryFee;

    @Column(name = "minimum_order", precision = 10, scale = 2)
    private BigDecimal minimumOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_admin_id")
    private AdminJpaEntity createdBy;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(nullable = false)
    @Builder.Default
    private Boolean open = false;

    @Column(precision = 3, scale = 2)
    @Builder.Default
    private BigDecimal rating = BigDecimal.ZERO;

    @Column(name = "total_ratings")
    @Builder.Default
    private Integer totalRatings = 0;

    public static StoreJpaEntity fromDomain(Store store) {
        if (store == null) return null;

        return StoreJpaEntity.builder()
                .id(store.getId())
                .name(store.getName())
                .description(store.getDescription())
                .city(store.getCity())
                .state(store.getState())
                .totalSales(store.getTotalSales())
                .phone(store.getPhone())
                .email(store.getEmail())
                .address(store.getAddress())
                .category(store.getCategory())
                .openingTime(store.getOpeningTime())
                .closingTime(store.getClosingTime())
                .deliveryFeePerKm(store.getDeliveryFeePerKm())
                .baseDeliveryFee(store.getBaseDeliveryFee())
                .minimumOrder(store.getMinimumOrder())
                .active(store.getActive())
                .open(store.getOpen())
                .rating(store.getRating())
                .totalRatings(store.getTotalRatings())
                .build();

    }

    public Store toDomain() {
        Store store = Store.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .city(this.city)
                .state(this.state)
                .totalSales(this.totalSales)
                .phone(this.phone)
                .email(this.email)
                .address(this.address)
                .category(this.category)
                .openingTime(this.openingTime)
                .closingTime(this.closingTime)
                .deliveryFeePerKm(this.deliveryFeePerKm)
                .baseDeliveryFee(this.baseDeliveryFee)
                .minimumOrder(this.minimumOrder)
                .active(true)
                .open(false)
                .rating(this.rating)
                .totalRatings(this.totalRatings)
                .build();

        return  store;
    }
}