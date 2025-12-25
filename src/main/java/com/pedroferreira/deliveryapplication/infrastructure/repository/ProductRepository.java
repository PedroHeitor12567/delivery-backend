package com.pedroferreira.deliveryapplication.infrastructure.repository;

import com.pedroferreira.deliveryapplication.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStoreId(Long storeId);

    List<Product> findByStoreIdAndAvailableTrue(Long storeId);

    List<Product> findByStoreIdAndActiveTrue(Long storeId);

    @Query("SELECT p FROM Product p WHERE p.store.id = :storeId " + "AND p.available = true AND p.active = true")
    List<Product> findAvailableProductsByStore(@Param("storeId") Long storeId);
}
