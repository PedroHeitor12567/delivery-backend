package com.pedroferreira.deliveryapplication.infrastructure.repository;

import com.pedroferreira.deliveryapplication.domain.entity.Admin;
import com.pedroferreira.deliveryapplication.domain.repository.AdminRepository;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.AdminJpaEntity;
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

    @Override
    public Admin save(Admin admin) {
        AdminJpaEntity jpaEntity = AdminJpaEntity.fromDomain(admin);
        AdminJpaEntity saved = jpaRepository.save(jpaEntity);
        return saved.toDomain();
    }

    @Override
    public Optional<Admin> findById(Long id) {
        return jpaRepository.findById(id)
                .map(AdminJpaEntity::toDomain);
    }

    @Override
    public Optional<Admin> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(AdminJpaEntity::toDomain);
    }

    @Override
    public List<Admin> findAll() {
        return jpaRepository.findAll().stream()
                .map(AdminJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Admin> findByActiveTrue() {
        return jpaRepository.findByActiveTrue().stream()
                .map(AdminJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Admin> findByFullAccessTrue() {
        return jpaRepository.findByFullAccessTrue().stream()
                .map(AdminJpaEntity::toDomain)
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
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
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