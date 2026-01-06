package com.pedroferreira.deliveryapplication.infrastructure.repository.impl;

import com.pedroferreira.deliveryapplication.domain.entity.SellerApplication;
import com.pedroferreira.deliveryapplication.domain.enuns.ApplicationStatus;
import com.pedroferreira.deliveryapplication.domain.repository.SellerApplicationRepository;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.SellerApplicationJpaEntity;
import com.pedroferreira.deliveryapplication.infrastructure.repository.SellerApplicationJpaRepository;
import com.pedroferreira.deliveryapplication.infrastructure.repository.mapper.SellerApplicationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SellerApplicationRepositoryImpl implements SellerApplicationRepository {

    private final SellerApplicationJpaRepository jpaRepository;
    private final SellerApplicationMapper mapper;

    @Override
    public SellerApplication save(SellerApplication application) {
        SellerApplicationJpaEntity jpaEntity = mapper.toJpaEntity(application);
        SellerApplicationJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<SellerApplication> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<SellerApplication> findByStatus(ApplicationStatus status) {
        return jpaRepository.findByStatus(status).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<SellerApplication> findByCustomerId(Long customerId) {
        return jpaRepository.findByCustomerId(customerId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsPendingApplicationForCustomer(Long customerId) {
        return jpaRepository.existsPendingApplicationForCustomer(customerId);
    }

    @Override
    public List<SellerApplication> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
