package com.pedroferreira.deliveryapplication.infrastructure.repository.mapper;

import com.pedroferreira.deliveryapplication.domain.entity.Store;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.StoreJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class StoreMapper {

    private final CityMapper cityMapper;
    private final AdminMapper adminMapper;

    public StoreMapper(CityMapper cityMapper, AdminMapper adminMapper) {
        this.cityMapper = cityMapper;
        this.adminMapper = adminMapper;
    }

    public Store toDomain(StoreJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;

        return Store.builder()
                .id(jpaEntity.getId())
                .name(jpaEntity.getName())
                .description(jpaEntity.getDescription())
                .city(cityMapper.toDomain(jpaEntity.getCity()))
                .totalSales(jpaEntity.getTotalSales())
                .phone(jpaEntity.getPhone())
                .email(jpaEntity.getEmail())
                .address(jpaEntity.getAddress())
                .category(jpaEntity.getCategory())
                .openingTime(jpaEntity.getOpeningTime())
                .closingTime(jpaEntity.getClosingTime())
                .deliveryFeePerKm(jpaEntity.getDeliveryFeePerKm())
                .baseDeliveryFee(jpaEntity.getBaseDeliveryFee())
                .minimumOrder(jpaEntity.getMinimumOrder())
                .active(jpaEntity.getActive())
                .open(jpaEntity.getOpen())
                .rating(jpaEntity.getRating())
                .totalRatings(jpaEntity.getTotalRatings())
                .createdBy(jpaEntity.getCreatedBy() != null ?
                        adminMapper.toDomain(jpaEntity.getCreatedBy()) : null)
                .build();
    }

    public StoreJpaEntity toJpaEntity(Store domain) {
        if (domain == null) return null;

        return StoreJpaEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .description(domain.getDescription())
                .city(cityMapper.toJpaEntity(domain.getCity()))
                .totalSales(domain.getTotalSales())
                .phone(domain.getPhone())
                .email(domain.getEmail())
                .address(domain.getAddress())
                .category(domain.getCategory())
                .openingTime(domain.getOpeningTime())
                .closingTime(domain.getClosingTime())
                .deliveryFeePerKm(domain.getDeliveryFeePerKm())
                .baseDeliveryFee(domain.getBaseDeliveryFee())
                .minimumOrder(domain.getMinimumOrder())
                .active(domain.getActive())
                .open(domain.getOpen())
                .rating(domain.getRating())
                .totalRatings(domain.getTotalRatings())
                .createdBy(domain.getCreatedBy() != null ?
                        adminMapper.toJpaEntity(domain.getCreatedBy()) : null)
                .build();
    }
}
