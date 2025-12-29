package com.pedroferreira.deliveryapplication.domain.entity;

import com.pedroferreira.deliveryapplication.domain.enuns.UserRole;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class User {

    @EqualsAndHashCode.Include
    private Long id;
    private String username;
    private String email;
    private String password;
    private String cpf;
    private String phone;
    private String address;
    private Boolean active;
    private UserRole role;
    private String oauthProvider;
    private String oauthId;

    protected User() {
        this.active = true;
    }

    protected User(String username, String email, String password, String cpf, String phone, String address, UserRole role) {
        validateConstructorParams(username, email, cpf, phone, address);
        this.username = username;
        this.email = email;
        this.password = password;
        this.cpf = cpf;
        this.phone = phone;
        this.address = address;
        this.active = true;
        this.role = role;
    }

    public void disable(){
        this.active = false;
    }

    public void enable(){
        this.active = true;
    }

    public boolean isActive(){
        return Boolean.TRUE.equals(this.active);
    }

    public abstract UserRole getUserRole();

    private void validateConstructorParams(String username, String email, String cpf, String phone, String address) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username é obrigatório");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF é obrigatório");
        }
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Telefone é obrigatório");
        }
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Endereço é obrigatório");
        }
    }
}
