package com.pedroferreira.deliveryapplication.domain.repository;

import com.pedroferreira.deliveryapplication.domain.entity.ItemOrder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemOrderRepository {
    ItemOrder save(ItemOrder itemOrder);
    Optional<ItemOrder> findById(Long id);
    List<ItemOrder> findByOrderId(Long orderId);
    List<ItemOrder> findByProductId(Long productId);
    List<ItemOrder> findAll();
    void delete(ItemOrder itemOrder);
}
