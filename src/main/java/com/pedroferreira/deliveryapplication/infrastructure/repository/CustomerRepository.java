package com.pedroferreira.deliveryapplication.infrastructure.repository;

import com.pedroferreira.deliveryapplication.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByCpf(String cpf);

    Optional<Customer> findByUsername(String username);

    Optional<Customer> findByOauthProviderAndOauthId(String provider, String oauthId);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    List<Customer> findByActiveTrue();
}
