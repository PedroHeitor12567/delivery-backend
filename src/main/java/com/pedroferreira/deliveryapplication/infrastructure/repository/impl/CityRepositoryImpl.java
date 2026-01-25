package com.pedroferreira.deliveryapplication.infrastructure.repository.impl;

import com.pedroferreira.deliveryapplication.domain.entity.City;
import com.pedroferreira.deliveryapplication.domain.repository.CityRepository;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.CityJpaEntity;
import com.pedroferreira.deliveryapplication.infrastructure.repository.mapper.CityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CityRepositoryImpl implements CityRepository {

    private final CityJpaRepositorySpring jpaRepository;
    private final CityMapper mapper;

    @Override
    public City save(City city) {
        CityJpaEntity jpaEntity = mapper.toJpaEntity(city);
        CityJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<City> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<City> findByNameAndState(String name, String state) {
        return jpaRepository.findByNameAndState(name, state)
                .map(mapper::toDomain);
    }

    @Override
    public List<City> findByState(String state) {
        return jpaRepository.findByState(state).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<City> findByActiveTrue() {
        return jpaRepository.findByActiveTrue().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<City> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByNameAndState(String name, String state) {
        return jpaRepository.existsByNameAndState(name, state);
    }

    @Override
    public void delete(City city) {
        if (city.getId() != null) {
            jpaRepository.deleteById(city.getId());
        }
    }

    @Override
    public Long count() {
        return jpaRepository.count();
    }
}

interface CityJpaRepositorySpring extends JpaRepository<CityJpaEntity, Long> {
    Optional<CityJpaEntity> findByNameAndState(String name, String state);
    List<CityJpaEntity> findByState(String state);
    List<CityJpaEntity> findByActiveTrue();
    boolean existsByNameAndState(String name, String state);
}