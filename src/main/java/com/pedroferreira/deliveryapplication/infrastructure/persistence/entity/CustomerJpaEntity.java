package com.pedroferreira.deliveryapplication.infrastructure.persistence.entity;

import com.pedroferreira.deliveryapplication.domain.entity.Customer;
import com.pedroferreira.deliveryapplication.domain.enuns.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerJpaEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Boolean active;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(name = "oauth_provider")
    private String oauthProvider;

    @Column(name = "oauth_id")
    private String oauthId;

    @Column(name = "loyalty_points")
    private Integer loyaltyPoints;

    @OneToMany(mappedBy = "customer")
    private List<OrderJpaEntity> orders = new ArrayList<>();

    public static CustomerJpaEntity fromDomain(Customer customer) {
        if (customer == null) return null;

        return CustomerJpaEntity.builder()
                .id(customer.getId())
                .username(customer.getUsername())
                .email(customer.getEmail())
                .password(customer.getPassword())
                .cpf(customer.getCpf())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .active(customer.getActive())
                .role(customer.getUserRole())
                .oauthProvider(customer.getOauthProvider())
                .oauthId(customer.getOauthId())
                .loyaltyPoints(customer.getLoyaltyPoints())
                .build();
    }

    public Customer toDomain() {
        Customer customer = Customer.builder()
                .id(this.id)
                .username(this.username)
                .email(this.email)
                .password(this.password)
                .cpf(this.cpf)
                .phone(this.phone)
                .address(this.address)
                .build();

        customer.setActive(this.active);
        customer.setRole(this.role);
        customer.setOauthProvider(this.oauthProvider);
        customer.setOauthId(this.oauthId);
        customer.setLoyaltyPoints(this.loyaltyPoints);

        return customer;
    }
}
