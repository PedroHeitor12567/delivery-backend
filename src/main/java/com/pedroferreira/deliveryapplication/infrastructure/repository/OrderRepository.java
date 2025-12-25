package com.pedroferreira.deliveryapplication.infrastructure.repository;

import com.pedroferreira.deliveryapplication.domain.entity.Order;
import com.pedroferreira.deliveryapplication.domain.enuns.StatusOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);

    List<Order> findByStoreId(Long storeId);

    List<Order> findByStatus(StatusOrder status);

    List<Order> findByCustomerIdAndStatus(Long customerId, StatusOrder status);

    List<Order> findByStoreIdAndStatus(Long storeId, StatusOrder status);

    @Query("SELECT o FROM Order o WHERE o.store.id = :storeId " + "and o.status IN :statuses ORDER BY o.createdAt DESC")
    List<Order> findByStoreIdAndStatusIn(@Param("storeId") Long storeId, @Param("statuses") List<StatusOrder> statuses);

    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId " + "ORDER BY o.createdAt DESC")
    List<Order> findByCustomerIdOrderByCreatedAtDesc(@Param("customerId") Long customerId);

    @Query("SELECT o FROM Order o WHERE o.status = :status " + "ORDER BY o.createdAt ASC")
    List<Order> findByStatusOrderByCreatedAtAsc(@Param("status") StatusOrder status);
}
