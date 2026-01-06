package com.pedroferreira.deliveryapplication.infrastructure.repository.impl;

import com.pedroferreira.deliveryapplication.domain.entity.Seller;
import com.pedroferreira.deliveryapplication.domain.repository.SellerRepository;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.SellerJpaEntity;
import com.pedroferreira.deliveryapplication.infrastructure.repository.mapper.SellerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SellerRepositoryImpl implements SellerRepository {

    private final SellerJpaRepositorySpring jpaRepository;
    private final SellerMapper mapper;

    @Override
    public Seller save(Seller seller) {
        SellerJpaEntity jpaEntity = mapper.toJpaEntity(seller);
        SellerJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Seller> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Seller> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Seller> findByCpf(String cpf) {
        return Optional.empty();
    }

    @Override
    public Optional<Seller> findByStoreId(Long storeId) {
        return jpaRepository.findByStoreId(storeId)
                .map(mapper::toDomain);
    }

    @Override
    public List<Seller> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return false;
    }

    @Override
    public void delete(Seller seller) {
        if (seller.getId() != null) {
            jpaRepository.deleteById(seller.getId());
        }
    }

    @Override
    public Long count() {
        return jpaRepository.count();
    }
}

interface SellerJpaRepositorySpring extends JpaRepository<SellerJpaEntity, Long> {
    Optional<SellerJpaEntity> findByEmail(String email);
    Optional<SellerJpaEntity> findByStoreId(Long storeId);
    boolean existsByEmail(String email);
}
