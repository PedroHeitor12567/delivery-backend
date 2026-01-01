package com.pedroferreira.deliveryapplication.infrastructure.repository;

import com.pedroferreira.deliveryapplication.domain.entity.Store;
import com.pedroferreira.deliveryapplication.domain.repository.StoreRespository;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.StoreJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRespository {

    private final StoreJpaRepositorySpring jpaRepository;

    @Override
    public Store save(Store store) {
        StoreJpaEntity jpaEntity = StoreJpaEntity.fromDomain(store);
        StoreJpaEntity saved = jpaRepository.save(jpaEntity);
        return saved.toDomain();
    }

    @Override
    public Optional<Store> findById(Long id) {
        return jpaRepository.findById(id)
                .map(StoreJpaEntity::toDomain);
    }

    @Override
    public Optional<Store> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(StoreJpaEntity::toDomain);
    }

    @Override
    public List<Store> findAll() {
        return jpaRepository.findAll().stream()
                .map(StoreJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Store> findByActiveTrue() {
        return jpaRepository.findByActiveTrue().stream()
                .map(StoreJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Store> findByActiveTrueAndOpenTrue() {
        return jpaRepository.findByActiveTrueAndOpenTrue().stream()
                .map(StoreJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Store> findByCategory(String category) {
        return jpaRepository.findByCategory(category).stream()
                .map(StoreJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Store> findByCityAndActiveTrue(String city) {
        return List.of();
    }

    @Override
    public List<Store> searchOpenStores(String search) {
        return List.of();
    }

    @Override
    public void delete(Store store) {
        if (store.getId() != null) {
            jpaRepository.deleteById(store.getId());
        }
    }

    @Override
    public Long count() {
        return jpaRepository.count();
    }
}

interface StoreJpaRepositorySpring extends JpaRepository<StoreJpaEntity, Long> {
    Optional<StoreJpaEntity> findByEmail(String email);
    List<StoreJpaEntity> findByActiveTrue();
    List<StoreJpaEntity> findByActiveTrueAndOpenTrue();
    List<StoreJpaEntity> findByCategory(String category);

    @Query("SELECT s FROM StoreJpaEntity s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<StoreJpaEntity> searchByNameContaining(@Param("name") String name);
}