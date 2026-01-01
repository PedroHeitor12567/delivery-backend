package com.pedroferreira.deliveryapplication.domain.repository;

import com.pedroferreira.deliveryapplication.domain.entity.Customer;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository {
    Customer save(Customer customer);
    Optional<Customer> findById(Long id);
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByCpf(String cpf);
    Optional<Customer> findByUsername(String username);
    Optional<Customer> findByOauthProviderAndOauthId(String provider, String oauthId);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    List<Customer> findByActiveTrue();
    List<Customer> findAll();
    void delete(Customer customer);
    Long count();
}
