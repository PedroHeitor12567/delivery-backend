package com.pedroferreira.deliveryapplication.infrastructure.repository.impl;

import com.pedroferreira.deliveryapplication.domain.entity.Address;
import com.pedroferreira.deliveryapplication.domain.repository.AddressRepository;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.AddressJpaEntity;
import com.pedroferreira.deliveryapplication.infrastructure.repository.mapper.AddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AddressRepositoryImpl implements AddressRepository {

    private final AddressJpaRepositorySpring jpaRepository;
    private final AddressMapper mapper;

    @Override
    public Address save(Address address) {
        AddressJpaEntity jpaEntity = mapper.toJpaEntity(address);
        AddressJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Address> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Address> findByCustomerId(Long customerId) {
        return jpaRepository.findByCustomerId(customerId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Address> findByCustomerIdAndCityId(Long customerId, Long cityId) {
        return jpaRepository.findByCustomerIdAndCityId(customerId, cityId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Address> findByCustomerIdAndIsDefaultTrue(Long customerId) {
        return jpaRepository.findByCustomerIdAndIsDefaultTrue(customerId)
                .map(mapper::toDomain);
    }

    @Override
    public List<Address> findByCustomerIdAndActiveTrue(Long customerId) {
        return jpaRepository.findByCustomerIdAndActiveTrue(customerId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Address> findByCityId(Long cityId) {
        return jpaRepository.findByCityId(cityId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Address> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Address address) {
        if (address.getId() != null) {
            jpaRepository.deleteById(address.getId());
        }
    }
}

interface AddressJpaRepositorySpring extends JpaRepository<AddressJpaEntity, Long> {
    List<AddressJpaEntity> findByCustomerId(Long customerId);
    List<AddressJpaEntity> findByCustomerIdAndCityId(Long customerId, Long cityId);
    Optional<AddressJpaEntity> findByCustomerIdAndIsDefaultTrue(Long customerId);
    List<AddressJpaEntity> findByCustomerIdAndActiveTrue(Long customerId);
    List<AddressJpaEntity> findByCityId(Long cityId);
}