package com.pedroferreira.deliveryapplication.infrastructure.repository;

import com.pedroferreira.deliveryapplication.domain.entity.Customer;
import com.pedroferreira.deliveryapplication.domain.repository.CustomerRepository;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.CustomerJpaEntity;
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

    @Override
    public Customer save(Customer customer) {
        CustomerJpaEntity jpaEntity = CustomerJpaEntity.fromDomain(customer);
        CustomerJpaEntity saved = jpaRepository.save(jpaEntity);
        return saved.toDomain();
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return jpaRepository.findById(id)
                .map(CustomerJpaEntity::toDomain);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(CustomerJpaEntity::toDomain);
    }

    @Override
    public Optional<Customer> findByCpf(String cpf) {
        return jpaRepository.findByCpf(cpf)
                .map(CustomerJpaEntity::toDomain);
    }

    @Override
    public Optional<Customer> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Optional<Customer> findByOauthProviderAndOauthId(String provider, String oauthId) {
        return jpaRepository.findByOauthProviderAndOauthId(provider, oauthId)
                .map(CustomerJpaEntity::toDomain);
    }

    @Override
    public List<Customer> findAll() {
        return jpaRepository.findAll().stream()
                .map(CustomerJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Customer> findByActiveTrue() {
        return jpaRepository.findByActiveTrue().stream()
                .map(CustomerJpaEntity::toDomain)
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