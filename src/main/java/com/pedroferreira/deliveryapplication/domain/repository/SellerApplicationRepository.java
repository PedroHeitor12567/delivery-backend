package com.pedroferreira.deliveryapplication.domain.repository;

import com.pedroferreira.deliveryapplication.domain.entity.SellerApplication;
import com.pedroferreira.deliveryapplication.domain.enuns.ApplicationStatus;

import java.util.List;
import java.util.Optional;

public interface SellerApplicationRepository {

    SellerApplication save(SellerApplication application);

    Optional<SellerApplication> findById(Long id);

    List<SellerApplication> findByStatus(ApplicationStatus status);

    List<SellerApplication> findByCustomerId(Long customerId);

    boolean existsPendingApplicationForCustomer(Long customerId);

    List<SellerApplication> findAll();
}
