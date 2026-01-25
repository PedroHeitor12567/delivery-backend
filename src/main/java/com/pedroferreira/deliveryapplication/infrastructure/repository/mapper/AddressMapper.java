package com.pedroferreira.deliveryapplication.infrastructure.repository.mapper;

import com.pedroferreira.deliveryapplication.domain.entity.Address;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.AddressJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressMapper {

    private final CustomerMapper customerMapper;
    private final CityMapper cityMapper;

    public Address toDomain(AddressJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;

        return Address.builder()
                .id(jpaEntity.getId())
                .customer(customerMapper.toDomain(jpaEntity.getCustomer()))
                .city(cityMapper.toDomain(jpaEntity.getCity()))
                .street(jpaEntity.getStreet())
                .number(jpaEntity.getNumber())
                .complement(jpaEntity.getComplement())
                .neighborhood(jpaEntity.getNeighborhood())
                .zipCode(jpaEntity.getZipCode())
                .reference(jpaEntity.getReference())
                .isDefault(jpaEntity.getIsDefault())
                .active(jpaEntity.getActive())
                .build();
    }

    public AddressJpaEntity toJpaEntity(Address domain) {
        if (domain == null) return null;

        return AddressJpaEntity.builder()
                .id(domain.getId())
                .customer(customerMapper.toJpaEntity(domain.getCustomer()))
                .city(cityMapper.toJpaEntity(domain.getCity()))
                .street(domain.getStreet())
                .number(domain.getNumber())
                .complement(domain.getComplement())
                .neighborhood(domain.getNeighborhood())
                .zipCode(domain.getZipCode())
                .reference(domain.getReference())
                .isDefault(domain.getIsDefault())
                .active(domain.getActive())
                .build();
    }
}