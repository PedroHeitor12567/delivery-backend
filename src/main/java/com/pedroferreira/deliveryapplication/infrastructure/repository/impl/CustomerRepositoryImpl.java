package com.pedroferreira.deliveryapplication.infrastructure.repository.impl;

import com.pedroferreira.deliveryapplication.domain.entity.Customer;
import com.pedroferreira.deliveryapplication.domain.repository.CustomerRepository;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.CustomerJpaEntity;
import com.pedroferreira.deliveryapplication.infrastructure.repository.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepositoryString jpaRepository;
    private final CustomerMapper mapper;

    @Override
    public Customer save(Customer customer) {
        CustomerJpaEntity jpaEntity = mapper.toJpaEntity(customer);
        CustomerJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Customer> findByCpf(String cpf) {
        return jpaRepository.findByCpf(cpf)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Customer> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Optional<Customer> findByOauthProviderAndOauthId(String provider, String oauthId) {
        return jpaRepository.findByOauthProviderAndOauthId(provider, oauthId)
                .map(mapper::toDomain);
    }

    @Override
    public List<Customer> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Customer> findByActiveTrue() {
        return jpaRepository.findByActiveTrue().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return jpaRepository.existsByCpf(cpf);
    }

    @Override
    public void delete(Customer customer) {
        if (customer.getId() != null) {
            jpaRepository.deleteById(customer.getId());
        }
    }

    @Override
    public Long count() {
        return jpaRepository.count();
    }
}

interface CustomerJpaRepositoryString extends JpaRepository<CustomerJpaEntity, Long> {
    Optional<CustomerJpaEntity> findByEmail(String email);
    Optional<CustomerJpaEntity> findByCpf(String cpf);
    Optional<CustomerJpaEntity> findByOauthProviderAndOauthId(String provider, String oauthId);
    List<CustomerJpaEntity> findByActiveTrue();
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
}