package com.pedroferreira.deliveryapplication.infrastructure.repository.mapper;

import com.pedroferreira.deliveryapplication.domain.entity.Customer;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.CustomerJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toDomain(CustomerJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;
        Customer customer = Customer.builder()
                .id(jpaEntity.getId())
                .username(jpaEntity.getUsername())
                .email(jpaEntity.getEmail())
                .password(jpaEntity.getPassword())
                .cpf(jpaEntity.getCpf())
                .phone(jpaEntity.getPhone())
                .address(jpaEntity.getAddress())
                .build();

        customer.setActive(jpaEntity.getActive());
        customer.setRole(jpaEntity.getRole());
        customer.setOauthProvider(jpaEntity.getOauthProvider());
        customer.setOauthId(jpaEntity.getOauthId());

        customer.setLoyaltyPoints(
                jpaEntity.getLoyaltyPoints() != null ? jpaEntity.getLoyaltyPoints() : 0
        );

        return customer;
    }

    public CustomerJpaEntity toJpaEntity(Customer domain) {
        if (domain == null) return null;

        return CustomerJpaEntity.builder()
                .id(domain.getId())
                .username(domain.getUsername())
                .email(domain.getEmail())
                .password(domain.getPassword())
                .cpf(domain.getCpf())
                .phone(domain.getPhone())
                .address(domain.getAddress())
                .active(domain.getActive())
                .role(domain.getUserRole())
                .oauthProvider(domain.getOauthProvider())
                .oauthId(domain.getOauthId())
                .loyaltyPoints(domain.getLoyaltyPoints())
                .build();
    }
}
