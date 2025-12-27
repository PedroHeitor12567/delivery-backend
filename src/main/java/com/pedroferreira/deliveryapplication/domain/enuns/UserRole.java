package com.pedroferreira.deliveryapplication.domain.enuns;

public enum UserRole {

    CUSTOMER,
    SELLER,
    ADMIN,
    SYSTEM;

    public boolean canStartOrder(){
        return this == CUSTOMER;
    }

    public boolean canOperateOrder(){
        return this == SELLER;
    }

    public boolean isAdmin(){
        return this == ADMIN;
    }

    public boolean ehSystem(){
        return this == SYSTEM;
    }
}
