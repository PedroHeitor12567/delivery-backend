package com.pedroferreira.deliveryapplication.domain.entity;

import com.pedroferreira.deliveryapplication.domain.enuns.UserRole;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Admin extends User {

    private Boolean fullAccess = true;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    @Builder
    public Admin(Long id, String username, String email, String password, String cpf, String phone, String address, Boolean fullAccess) {
        super(username, email, password, cpf, phone, address, UserRole.ADMIN);
        this.setId(id);
        this.fullAccess = fullAccess != null ? fullAccess : true;
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public UserRole getUserRole() {
        return UserRole.ADMIN;
    }

    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now();
    }

    public boolean canManageStores() {
        return Boolean.TRUE.equals(this.fullAccess) && this.isActive();
    }

    public boolean canAccessStores() {
        return this.isActive();
    }
}
