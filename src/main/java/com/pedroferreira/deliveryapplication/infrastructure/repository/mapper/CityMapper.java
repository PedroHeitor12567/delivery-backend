package com.pedroferreira.deliveryapplication.infrastructure.repository.mapper;

import com.pedroferreira.deliveryapplication.domain.entity.City;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.CityJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class CityMapper {

    public City toDomain(CityJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;

        return City.builder()
                .id(jpaEntity.getId())
                .name(jpaEntity.getName())
                .state(jpaEntity.getState())
                .active(jpaEntity.getActive())
                .build();
    }

    public CityJpaEntity toJpaEntity(City domain) {
        if (domain == null) return null;

        return CityJpaEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .state(domain.getState())
                .active(domain.getActive())
                .build();
    }
}
