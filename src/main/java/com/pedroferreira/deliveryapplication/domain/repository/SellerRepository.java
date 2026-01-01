package com.pedroferreira.deliveryapplication.domain.repository;

import com.pedroferreira.deliveryapplication.domain.entity.Seller;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepository {
    Seller save(Seller seller);
    Optional<Seller> findById(Long id);
    Optional<Seller> findByStoreId(Long storeId);
    Optional<Seller> findByEmail(String email);
    Optional<Seller> findByCpf(String cpf);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    List<Seller> findAll();
    void delete(Seller seller);
    Long count();
}
