package com.pedroferreira.deliveryapplication.infrastructure.repository.mapper;

import com.pedroferreira.deliveryapplication.domain.entity.Seller;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.SellerJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class SellerMapper {

    private final StoreMapper storeMapper;

    public SellerMapper(StoreMapper storeMapper) {
        this.storeMapper = storeMapper;
    }

    public Seller toDomain(SellerJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;

        Seller seller = Seller.builder()
                .id(jpaEntity.getId())
                .username(jpaEntity.getUsername())
                .email(jpaEntity.getEmail())
                .password(jpaEntity.getPassword())
                .cpf(jpaEntity.getCpf())
                .phone(jpaEntity.getPhone())
                .address(jpaEntity.getAddress())
                .build();

        seller.setActive(jpaEntity.getActive());
        seller.setRole(jpaEntity.getRole());

        if (jpaEntity.getStore() != null) {
            seller.setStore(
                    storeMapper.toDomain(jpaEntity.getStore())
            );
        }

        return seller;
    }

    public SellerJpaEntity toJpaEntity(Seller domain) {
        if (domain == null) return null;

        return SellerJpaEntity.builder()
                .id(domain.getId())
                .username(domain.getUsername())
                .email(domain.getEmail())
                .password(domain.getPassword())
                .cpf(domain.getCpf())
                .phone(domain.getPhone())
                .address(domain.getAddress())
                .active(domain.getActive())
                .role(domain.getRole())
                .store(
                        domain.getStore() != null
                                ? storeMapper.toJpaEntity(domain.getStore())
                                : null
                )
                .build();
    }
}
