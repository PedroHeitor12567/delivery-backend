package com.pedroferreira.deliveryapplication.domain.entity;

public abstract class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String cpf;
    private String phone;
    private String adress;
    private boolean active;

    protected User(Long id, String username, String email, String password, String cpf, String phone, String adress) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.cpf = cpf;
        this.phone = phone;
        this.adress = adress;
        this.active = true;
    }

    public void disable(){
        this.active = false;
    }

    public boolean isAtivo(){
        return this.active;
    }
}
