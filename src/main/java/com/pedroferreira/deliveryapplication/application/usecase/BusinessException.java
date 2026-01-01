package com.pedroferreira.deliveryapplication.application.usecase;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
