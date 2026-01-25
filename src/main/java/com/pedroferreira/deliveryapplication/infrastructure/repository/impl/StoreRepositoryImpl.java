package com.pedroferreira.deliveryapplication.infrastructure.repository.impl;

import com.pedroferreira.deliveryapplication.domain.entity.Store;
import com.pedroferreira.deliveryapplication.domain.repository.StoreRespository;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.CityJpaEntity;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.StoreJpaEntity;
import com.pedroferreira.deliveryapplication.infrastructure.repository.mapper.CityMapper;
import com.pedroferreira.deliveryapplication.infrastructure.repository.mapper.StoreMapper;
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
    private final CityJpaRepositorySpring cityJpaRepository;
    private final StoreMapper mapper;
    private final CityMapper cityMapper;

    @Override
    public Store save(Store store) {
        StoreJpaEntity jpaEntity = mapper.toJpaEntity(store);

        if (store.getCity() != null && store.getCity().getId() != null) {
            CityJpaEntity cityJpa = cityJpaRepository.findById(store.getCity().getId())
                    .orElseThrow(() -> new RuntimeException("City not found: " + store.getCity().getId()));
            jpaEntity.setCity(cityJpa);
        }

        StoreJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Store> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Store> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public List<Store> findByActiveTrue() {
        return jpaRepository.findByActiveTrue().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Store> findByActiveTrueAndOpenTrue() {
        return jpaRepository.findByActiveTrueAndOpenTrue().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Store> findByCategory(String category) {
        return jpaRepository.findByCategory(category).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Store> findByCityIdAndActiveTrue(Long cityId) {
        return jpaRepository.findByCityIdAndActiveTrue(cityId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Store> findByCityIdAndActiveTrueAndOpenTrue(Long cityId) {
        return jpaRepository.findByCityIdAndActiveTrueAndOpenTrue(cityId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Deprecated
    public List<Store> findByCityAndActiveTrue(String cityName) {
        return jpaRepository.findByCityNameAndActiveTrue(cityName).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Store> searchOpenStores(String search) {
        return jpaRepository.searchOpenStores(search).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Store> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
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

    List<StoreJpaEntity> findByCityIdAndActiveTrue(Long cityId);
    List<StoreJpaEntity> findByCityIdAndActiveTrueAndOpenTrue(Long cityId);

    @Query("SELECT s FROM StoreJpaEntity s WHERE s.city.name = :cityName AND s.active = true")
    List<StoreJpaEntity> findByCityNameAndActiveTrue(@Param("cityName") String cityName);

    @Query("SELECT s FROM StoreJpaEntity s WHERE s.active = true AND s.open = true " +
            "AND (LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(s.category) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<StoreJpaEntity> searchOpenStores(@Param("search") String search);
}