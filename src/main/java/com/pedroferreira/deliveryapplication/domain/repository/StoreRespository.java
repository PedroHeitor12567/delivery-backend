package com.pedroferreira.deliveryapplication.domain.repository;

import com.pedroferreira.deliveryapplication.domain.entity.Store;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRespository {
    Store save(Store store);
    Optional<Store> findById(Long id);
    Optional<Store> findByEmail(String email);
    List<Store> findByActiveTrue();
    List<Store> findByActiveTrueAndOpenTrue();
    List<Store> findByCategory(String category);
    List<Store> findByCityAndActiveTrue(String city);
    List<Store> searchOpenStores(String search);
    List<Store> findAll();
    void delete(Store store);
    Long count();
}
