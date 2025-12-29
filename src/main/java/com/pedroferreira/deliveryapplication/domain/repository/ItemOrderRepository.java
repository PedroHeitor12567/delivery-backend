package com.pedroferreira.deliveryapplication.domain.repository;

import com.pedroferreira.deliveryapplication.domain.entity.ItemOrder;

import java.util.List;
import java.util.Optional;

public interface ItemOrderRepository {
    ItemOrder save(ItemOrder itemOrder);
    Optional<ItemOrder> findById(Long id);
    List<ItemOrder> findByOrderId(Long id);
    List<ItemOrder> findByProductId(Long id);
    void delete(ItemOrder itemOrder);
    void deleteById(Long id);
}
