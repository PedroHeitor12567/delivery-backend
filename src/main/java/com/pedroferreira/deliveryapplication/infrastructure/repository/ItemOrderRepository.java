package com.pedroferreira.deliveryapplication.infrastructure.repository;

import com.pedroferreira.deliveryapplication.domain.entity.ItemOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemOrderRepository extends JpaRepository<ItemOrder, Long> {

    List<ItemOrder> findByOrderId(Long orderId);

    @Query("SELECT i FROM ItemOrder i WHERE i.product.id = :productId")
    List<ItemOrder> findByProductId(@Param("productId") Long productId);
}
