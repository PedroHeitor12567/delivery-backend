package com.pedroferreira.deliveryapplication.domain.repository;

import com.pedroferreira.deliveryapplication.domain.entity.Store;

import java.util.List;
import java.util.Optional;

public interface StoreRespository {
    Store save(Store store);
    Optional<Store> findById(Long id);
    Optional<Store> findByEmail(String email);
    List<Store> findAll();
    List<Store> findByActiveTrue();
    List<Store> findByActiveTrueAndOpenTrue();
    List<Store> findByCategory(String category);
    List<Store> seacrhName(String name);
    
}
