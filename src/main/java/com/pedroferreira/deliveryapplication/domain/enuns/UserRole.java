package com.pedroferreira.deliveryapplication.domain.enuns;

public enum UserRole {

    CUSTOMER,
    SELLER,
    SYSTEM;

    public boolean canStartOrder(){
        return this == CUSTOMER;
    }

    public boolean canOperateOrder(){
        return this == SELLER;
    }

    public boolean ehSystem(){
        return this == SYSTEM;
    }
}
