package com.pedroferreira.deliveryapplication.domain.entity;

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
@EqualsAndHashCode(callSuper = true)
public class Customer extends User{

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @Column(name = "loyalty_points")
    private Integer loyaltyPoints = 0;

    @Builder
    public Customer(Long id, String username, String email, String password, String cpf, String phone, String address) {
        super(id, username, email, password, cpf, phone, address);
        this.loyaltyPoints = 0;
    }

    @Override
    public UserRole getUserRole() {
        return UserRole.CUSTOMER;
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.setCustomer(this);
    }

    public void addLoyaltyPoints(Integer points) {
        this.loyaltyPoints += points;
    }

    public void useLoyaltyPoints(Integer points) {
        if (this.loyaltyPoints < points) {
            throw new IllegalStateException("Pontos de fidelidade insuficientes");
        }
        this.loyaltyPoints -= points;
    }
}
