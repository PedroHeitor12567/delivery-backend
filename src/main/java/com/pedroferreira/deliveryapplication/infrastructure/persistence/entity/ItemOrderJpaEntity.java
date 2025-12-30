package com.pedroferreira.deliveryapplication.infrastructure.persistence.entity;

import com.pedroferreira.deliveryapplication.domain.entity.ItemOrder;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemOrderJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderJpaEntity order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductJpaEntity product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal discount;

    @Column(columnDefinition = "TEXT")
    private String observations;

    public static ItemOrderJpaEntity fromDomain(ItemOrder itemOrder) {
        if (itemOrder == null) return null;

        return ItemOrderJpaEntity.builder()
                .id(itemOrder.getId())
                .quantity(itemOrder.getQuantity())
                .unitPrice(itemOrder.getUnitPrice())
                .discount(itemOrder.getDiscount())
                .observations(itemOrder.getObservations())
                .build();
    }

    public ItemOrder toDomain() {
        ItemOrder itemOrder = ItemOrder.builder()
                .id(this.id)
                .quantity(this.quantity)
                .unitPrice(this.unitPrice)
                .discount(this.discount)
                .observations(this.observations)
                .build();

        if (this.product != null) {
            itemOrder.setProduct(this.product.toDomain());
        }

        return itemOrder;
    }
}
