package com.pedroferreira.deliveryapplication.infrastructure.repository.mapper;

import com.pedroferreira.deliveryapplication.domain.entity.ItemOrder;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.ItemOrderJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ItemOrderMapper {

    private final ProductMapper productMapper;

    public ItemOrderMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public ItemOrder toDomain(ItemOrderJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;

        ItemOrder itemOrder = ItemOrder.builder()
                .id(jpaEntity.getId())
                .quantity(jpaEntity.getQuantity())
                .unitPrice(jpaEntity.getUnitPrice())
                .discount(jpaEntity.getDiscount())
                .observations(jpaEntity.getObservations())
                .build();

        if (jpaEntity.getProduct() != null) {
            itemOrder.setProduct(
                    productMapper.toDomain(jpaEntity.getProduct())
            );
        }

        return itemOrder;
    }

    public ItemOrderJpaEntity toJpaEntity(ItemOrder domain) {
        if (domain == null) return null;

        return ItemOrderJpaEntity.builder()
                .id(domain.getId())
                .quantity(domain.getQuantity())
                .unitPrice(domain.getUnitPrice())
                .discount(domain.getDiscount())
                .observations(domain.getObservations())
                .product(
                        domain.getProduct() != null
                                ? productMapper.toJpaEntity(domain.getProduct())
                                : null
                )
                .build();
    }
}
