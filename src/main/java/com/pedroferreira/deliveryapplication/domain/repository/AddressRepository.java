package com.pedroferreira.deliveryapplication.domain.repository;

import com.pedroferreira.deliveryapplication.domain.entity.Address;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository {
    Address save(Address address);
    Optional<Address> findById(Long id);
    List<Address> findByCustomerId(Long customerId);
    List<Address> findByCustomerIdAndCityId(Long customerId, Long cityId);
    Optional<Address> findByCustomerIdAndIsDefaultTrue(Long customerId);
    List<Address> findByCustomerIdAndActiveTrue(Long customerId);
    List<Address> findByCityId(Long cityId);
    List<Address> findAll();
    void delete(Address address);
}
