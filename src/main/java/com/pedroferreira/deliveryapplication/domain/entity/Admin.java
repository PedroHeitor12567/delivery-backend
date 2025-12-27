package com.pedroferreira.deliveryapplication.domain.entity;

import com.pedroferreira.deliveryapplication.domain.enuns.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "admins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Admin extends User {

    @Column(name = "full_access")
    private Boolean fullAccess = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Builder
    public Admin(Long id, String username, String email, String password, String cpf, String phone, String address, Boolean fullAccess) {
        super(id, username, email, password, cpf, phone, address, UserRole.ADMIN);
        this.fullAccess = fullAccess != null ? fullAccess : true;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @Override
    public UserRole getUserRole() {
        return UserRole.ADMIN;
    }

    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now();
    }

    public boolean canManageStores() {
        return this.fullAccess && this.isActive();
    }

    public boolean canAccessStores() {
        return this.isActive();
    }
}
