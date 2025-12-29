package com.pedroferreira.deliveryapplication.domain.repository;

import com.pedroferreira.deliveryapplication.domain.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll();
    List<Product> findByStoreId(Long storeId);
    List<Product> findByStoreIdAndAvailableTrue(Long storeId);
    List<Product> findByStoreIdAndActiveTrue(Long storeId);
    Long countByAvailableTrue();
    void delete(Product product);
    void deleteById(Long id);
    Long count();
}
