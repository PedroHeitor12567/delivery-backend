package com.pedroferreira.deliveryapplication.domain.repository;

import com.pedroferreira.deliveryapplication.domain.entity.Order;
import com.pedroferreira.deliveryapplication.domain.enuns.StatusOrder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRespository {
    Order save(Order order);
    Optional<Order> findById(Long id);
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByStoreId(Long storeId);
    List<Order> findByStatus(StatusOrder status);
    List<Order> findByCustomerIdAndStatus(Long customerId, StatusOrder status);
    List<Order> findByStoreIdAndStatus(Long storeId, StatusOrder status);
    List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    Long countByCreatedAtAfter(LocalDateTime date);
    List<Order> findByStoreIdAndStatusIn(Long storeId, List<StatusOrder> statuses);
    List<Order> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
    List<Order> findByStatusOrderByCreatedAtAsc(StatusOrder status);
    List<Order> findAll();
    void delete(Order order);
    Long count();
}
