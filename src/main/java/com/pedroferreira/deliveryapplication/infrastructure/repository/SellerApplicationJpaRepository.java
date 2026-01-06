package com.pedroferreira.deliveryapplication.infrastructure.repository;

import com.pedroferreira.deliveryapplication.domain.enuns.ApplicationStatus;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.CustomerJpaEntity;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.SellerApplicationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerApplicationJpaRepository extends JpaRepository<SellerApplicationJpaEntity, Long> {

    List<SellerApplicationJpaEntity> findByStatus(ApplicationStatus status);

    List<SellerApplicationJpaEntity> findByCustomerId(Long customerId);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM SellerApplicationJpaEntity a " + "WHERE a.customer.id = :customerId AND a.status = 'PENDING'")
    boolean existsPendingApplicationForCustomer(@Param("customerId") Long customerId);

    Long customer(CustomerJpaEntity customer);
}
