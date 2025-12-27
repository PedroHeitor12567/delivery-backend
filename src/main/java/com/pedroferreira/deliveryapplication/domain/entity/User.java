package com.pedroferreira.deliveryapplication.domain.entity;

import com.pedroferreira.deliveryapplication.domain.enuns.UserRole;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
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

    protected User() {

    }

    protected User(Long id, String username, String email, String password, String cpf, String phone, String address, UserRole role) {
        this.id = id;
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
        return this.active;
    }

    public abstract UserRole getUserRole();
}
