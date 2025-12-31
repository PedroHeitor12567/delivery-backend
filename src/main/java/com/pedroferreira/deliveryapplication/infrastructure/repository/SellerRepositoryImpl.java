package com.pedroferreira.deliveryapplication.infrastructure.repository;

import com.pedroferreira.deliveryapplication.domain.entity.Seller;
import com.pedroferreira.deliveryapplication.domain.repository.SellerRepository;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.SellerJpaEntity;
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

    @Override
    public Seller save(Seller seller) {
        SellerJpaEntity jpaEntity = SellerJpaEntity.fromDomain(seller);
        SellerJpaEntity saved = jpaRepository.save(jpaEntity);
        return saved.toDomain();
    }

    @Override
    public Optional<Seller> findById(Long id) {
        return jpaRepository.findById(id)
                .map(SellerJpaEntity::toDomain);
    }

    @Override
    public Optional<Seller> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(SellerJpaEntity::toDomain);
    }

    @Override
    public Optional<Seller> findByStoreId(Long storeId) {
        return jpaRepository.findByStoreId(storeId)
                .map(SellerJpaEntity::toDomain);
    }

    @Override
    public List<Seller> findAll() {
        return jpaRepository.findAll().stream()
                .map(SellerJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public void delete(Seller seller) {
        if (seller.getId() != null) {
            jpaRepository.deleteById(seller.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
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
