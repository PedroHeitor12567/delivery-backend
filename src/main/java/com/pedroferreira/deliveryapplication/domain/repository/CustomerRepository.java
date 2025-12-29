package com.pedroferreira.deliveryapplication.domain.repository;

import com.pedroferreira.deliveryapplication.domain.entity.Customer;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    Customer save(Customer customer);
    Optional<Customer> findById(Long id);
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByCpf(String cpf);
    Optional<Customer> findByOauthProviderAndOauthId(String provider, String oauthId);
    List<Customer> findAll();
    List<Customer> findByActiveTrue();
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    void delete(Customer customer);
    void deleteById(Long id);
    Long count();
}
