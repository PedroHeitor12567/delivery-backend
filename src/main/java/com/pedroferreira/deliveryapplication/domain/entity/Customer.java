package com.pedroferreira.deliveryapplication.domain.entity;

import java.util.List;

public class Customer extends User{

    private List<Order> orders;

    public Customer(Long id, String username, String email, String password, String cpf, String phone, String address) {
        super(id, username, email, password, cpf, phone, address);
    }
}
