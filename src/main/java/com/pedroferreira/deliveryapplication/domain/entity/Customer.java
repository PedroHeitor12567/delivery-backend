package com.pedroferreira.deliveryapplication.domain.entity;

import com.pedroferreira.deliveryapplication.domain.enuns.UserRole;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Customer extends User{

    private List<Order> orders = new ArrayList<>();
    private Integer loyaltyPoints = 0;

    @Builder
    public Customer(Long id, String username, String email, String password, String cpf, String phone, String address) {
        super(id, username, email, password, cpf, phone, address, UserRole.CUSTOMER);
        this.setId(id);
        this.orders = new ArrayList<>();
        this.loyaltyPoints = 0;
    }

    public void addOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Pedido não pode ser nulo");
        }
        this.orders.add(order);
    }

    public void addLoyaltyPoints(Integer points) {
        if (points == null || points <= 0) {
            throw new IllegalArgumentException("Pontos devem ser positivos");
        }
        this.loyaltyPoints += points;
    }

    public void useLoyaltyPoints(Integer points) {
        if (points == null || this.loyaltyPoints < points) {
            throw new IllegalStateException("Pontos de fidelidade insuficientes");
        }
        this.loyaltyPoints -= points;
    }

    @Override
    public UserRole getUserRole() {
        return UserRole.CUSTOMER;
    }
}
