package com.pedroferreira.deliveryapplication.infrastructure.persistence.entity;

import com.pedroferreira.deliveryapplication.domain.entity.Product;
import com.pedroferreira.deliveryapplication.domain.entity.Store;
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
public class ProductJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreJpaEntity store;

    @Column(nullable = false)
    private Boolean available = true;

    @Column(name = "preparation_time")
    private Integer preparationTime;

    @Column(nullable = false)
    private Boolean active;

    public static ProductJpaEntity fromDomain(Product product) {
        if (product == null) return null;

        return ProductJpaEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .available(product.getAvailable())
                .preparationTime(product.getPreparationTime())
                .active(product.getActive())
                .build();
    }

    public Product toDomain() {
        Product product = Product.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .price(this.price)
                .imageUrl(this.imageUrl)
                .available(this.available)
                .preparationTime(this.preparationTime)
                .active(this.active)
                .build();

        if (this.store != null) {
            product.setStore(this.store.toDomain());
        }

        return product;
    }
}
