package com.pedroferreira.deliveryapplication.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Time;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Product> products = new ArrayList<>();

    @Column(name = "total_sales")
    @Builder.Default
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

    @Column(name = "delivery_fee", precision = 10, scale = 2)
    private BigDecimal deliveryFee;

    @Column(name = "minimum_order", precision = 10, scale = 2)
    private BigDecimal minimumOrder;

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

    public void addProduct(Product product) {
        products.add(product);
        product.setStore(this);
    }

    public void removeProduct(Product product) {
        products.remove(product);
        product.setStore(null);
    }

    public void incrementSales() {
        this.totalSales++;
    }

    public void addRating(Integer stars) {
        if (stars < 1 || stars > 5) {
            throw new IllegalArgumentException("Avaliação deve ser entre 1 e 5");
        }
        BigDecimal totalPoints = rating.multiply(BigDecimal.valueOf(totalRatings));
        totalRatings++;
        this.rating = totalPoints.add(BigDecimal.valueOf(stars)).divide(BigDecimal.valueOf(totalRatings), 2, RoundingMode.HALF_UP);
    }

    public boolean isOpenNow() {
        if (!active || !open) {
            return false;
        }
        LocalTime now = LocalTime.now();
        return now.isAfter(openingTime) && now.isBefore(closingTime);
    }

    public void openStore() {
        if (!active) {
            throw new IllegalStateException("Loja desativada não pode ser aberta");
        }
        this.open = true;
    }

    public void closeStore() {
        this.open = false;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
        this.open = false;
    }
}
