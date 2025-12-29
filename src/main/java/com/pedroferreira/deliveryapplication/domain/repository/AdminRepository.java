package com.pedroferreira.deliveryapplication.domain.repository;

import com.pedroferreira.deliveryapplication.domain.entity.Admin;

import java.util.List;
import java.util.Optional;

public interface AdminRepository {
    Admin save(Admin admin);
    Optional<Admin> findById(Long id);
    Optional<Admin> findByEmail(String email);
    List<Admin> findAll();
    List<Admin> findByActiveTrue();
    List<Admin> findByFullAccessTrue();
    boolean existsByEmail(String email);
    void delete(Admin admin);
    void deleteById(Long id);
    Long count();
}

