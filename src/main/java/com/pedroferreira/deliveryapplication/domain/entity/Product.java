package com.pedroferreira.deliveryapplication.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name="products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private Boolean available = true;

    @Column(name = "preparation_time")
    private Integer preparationTime;

    @Column(nullable = false)
    private Boolean active = true;

    public void activate() {
        this.active = true;
        this.available = true;
    }

    public void deactivate() {
        this.active = false;
        this.available = false;
    }

    public void makeUnavailable() {
        this.available = false;
    }

    public void makeAvailable() {
        if (this.active) {
            this.available = true;
        }
    }
}
