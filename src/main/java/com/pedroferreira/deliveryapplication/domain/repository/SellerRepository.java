package com.pedroferreira.deliveryapplication.domain.repository;

import com.pedroferreira.deliveryapplication.domain.entity.Seller;

import java.util.List;
import java.util.Optional;

public interface SellerRepository {
    Seller save(Seller seller);
    Optional<Seller> findById(Long id);
    Optional<Seller> findByEmail(String email);
    Optional<Seller> findByStoreId(Long storeId);
    List<Seller> findAll();
    boolean existsByEmail(String email);
    void delete(Seller seller);
    void deleteById(Long id);
    Long count();
}
