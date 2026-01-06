package com.pedroferreira.deliveryapplication.infrastructure.repository.mapper;

import com.pedroferreira.deliveryapplication.domain.entity.Product;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.ProductJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    private final StoreMapper storeMapper;

    public ProductMapper(StoreMapper storeMapper) {
        this.storeMapper = storeMapper;
    }

    public Product toDomain(ProductJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;

        Product product = Product.builder()
                .id(jpaEntity.getId())
                .name(jpaEntity.getName())
                .description(jpaEntity.getDescription())
                .price(jpaEntity.getPrice())
                .imageUrl(jpaEntity.getImageUrl())
                .available(jpaEntity.getAvailable())
                .preparationTime(jpaEntity.getPreparationTime())
                .active(jpaEntity.getActive())
                .build();

        if (jpaEntity.getStore() != null) {
            product.setStore(
                    storeMapper.toDomain(jpaEntity.getStore())
            );
        }

        return product;
    }

    public ProductJpaEntity toJpaEntity(Product domain) {
        if (domain == null) return null;

        return ProductJpaEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .description(domain.getDescription())
                .price(domain.getPrice())
                .imageUrl(domain.getImageUrl())
                .available(domain.getAvailable())
                .preparationTime(domain.getPreparationTime())
                .active(domain.getActive())
                .store(
                        domain.getStore() != null
                                ? storeMapper.toJpaEntity(domain.getStore())
                                : null
                )
                .build();
    }
}
