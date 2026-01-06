package com.pedroferreira.deliveryapplication.infrastructure.repository.impl;

import com.pedroferreira.deliveryapplication.domain.entity.Admin;
import com.pedroferreira.deliveryapplication.domain.repository.AdminRepository;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.AdminJpaEntity;
import com.pedroferreira.deliveryapplication.infrastructure.repository.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AdminRepositoryImpl implements AdminRepository {

    private final AdminJpaRepositorySpring jpaRepository;
    private final AdminMapper mapper;

    @Override
    public Admin save(Admin admin) {
        AdminJpaEntity jpaEntity = mapper.toJpaEntity(admin);
        AdminJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Admin> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Admin> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Admin> findByCpf(String cpf) {
        return Optional.empty();
    }

    @Override
    public List<Admin> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Admin> findByActiveTrue() {
        return jpaRepository.findByActiveTrue().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Admin> findByFullAccessTrue() {
        return jpaRepository.findByFullAccessTrue().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public void delete(Admin admin) {
        if (admin.getId() != null) {
            jpaRepository.deleteById(admin.getId());
        }
    }

    @Override
    public Long count() {
        return jpaRepository.count();
    }
}

interface AdminJpaRepositorySpring extends JpaRepository<AdminJpaEntity,Long> {
    Optional<AdminJpaEntity> findByEmail(String email);
    List<AdminJpaEntity> findByActiveTrue();
    List<AdminJpaEntity> findByFullAccessTrue();
    boolean existsByEmail(String email);
}