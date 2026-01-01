package com.pedroferreira.deliveryapplication.domain.repository;

import com.pedroferreira.deliveryapplication.domain.entity.Admin;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository {
    Admin save(Admin admin);
    Optional<Admin> findById(Long id);
    Optional<Admin> findByEmail(String email);
    Optional<Admin> findByCpf(String cpf);
    boolean existsByEmail(String email);
    List<Admin> findByActiveTrue();
    List<Admin> findByFullAccessTrue();
    List<Admin> findAll();
    void delete(Admin admin);
    Long count();
}

