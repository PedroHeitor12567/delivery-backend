package com.pedroferreira.deliveryapplication.infrastructure.persistence.entity;

import com.pedroferreira.deliveryapplication.domain.entity.Admin;
import com.pedroferreira.deliveryapplication.domain.enuns.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "admins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 11)
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

    @Column(name = "full_access")
    private Boolean fullAccess;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    public static AdminJpaEntity fromDomain(Admin admin) {
        if (admin == null) return null;

        return AdminJpaEntity.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .email(admin.getEmail())
                .password(admin.getPassword())
                .cpf(admin.getCpf())
                .phone(admin.getPhone())
                .address(admin.getAddress())
                .active(admin.getActive())
                .role(admin.getRole())
                .fullAccess(admin.getFullAccess())
                .createdAt(admin.getCreatedAt())
                .lastLogin(admin.getLastLogin())
                .build();
    }

    public Admin toDomain() {
        Admin admin = Admin.builder()
                .id(this.id)
                .username(this.username)
                .email(this.email)
                .password(this.password)
                .cpf(this.cpf)
                .phone(this.phone)
                .address(this.address)
                .fullAccess(this.fullAccess)
                .build();

        admin.setActive(this.active);
        admin.setRole(this.role);
        admin.setCreatedAt(this.createdAt);
        admin.setLastLogin(this.lastLogin);

        return admin;
    }
}
