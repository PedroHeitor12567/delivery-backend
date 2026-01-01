package com.pedroferreira.deliveryapplication.domain.repository;

import com.pedroferreira.deliveryapplication.domain.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Long id);
    List<Product> findByStoreId(Long storeId);
    List<Product> findByStoreIdAndAvailableTrue(Long storeId);
    List<Product> findByStoreIdAndActiveTrue(Long storeId);
    List<Product> findAvailableProductsByStore(Long storeId);
    Long countByAvailableTrue();
    List<Product> findAll();
    void delete(Product product);
    Long count();
}
