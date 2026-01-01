package com.pedroferreira.deliveryapplication.application.usecase;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
