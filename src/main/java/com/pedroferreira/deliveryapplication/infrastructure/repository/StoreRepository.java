package com.pedroferreira.deliveryapplication.infrastructure.repository;

import com.pedroferreira.deliveryapplication.domain.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findByActiveTrue();

    List<Store> findByActiveTrueAndOpenTrue();

    List<Store> findByCategory(String category);

    List<Store> findByCityAndActiveTrue(String city);

    @Query("SELECT s FROM Store s WHERE s.active = true AND s.open = true " + "AND LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Store> searchOpenStores(@Param("search") String search);

    Optional<Store> findByEmail(String email);
}
