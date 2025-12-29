package com.pedroferreira.deliveryapplication.domain.repository;

import com.pedroferreira.deliveryapplication.domain.entity.Order;
import com.pedroferreira.deliveryapplication.domain.enuns.StatusOrder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRespository {
    Order save(Order order);
    Optional<Order> findById(Long id);
    List<Order> findAll();
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByStoreId(Long storeId);
    List<Order> findByStatus(StatusOrder status);
    List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    Long countByCreatedAtAfter(LocalDateTime date);
    void delete(Order order);
    void deleteById(Long id);
    Long count();
}
