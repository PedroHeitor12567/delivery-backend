package com.pedroferreira.deliveryapplication.infrastructure.repository.mapper;

import com.pedroferreira.deliveryapplication.domain.entity.Admin;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.AdminJpaEntity;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.CustomerJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class AdminMapper {

    public Admin toDomain(AdminJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;

        Admin admin = Admin.builder()
                .id(jpaEntity.getId())
                .username(jpaEntity.getUsername())
                .email(jpaEntity.getEmail())
                .password(jpaEntity.getPassword())
                .cpf(jpaEntity.getCpf())
                .phone(jpaEntity.getPhone())
                .address(jpaEntity.getAddress())
                .fullAccess(jpaEntity.getFullAccess())
                .build();

        admin.setActive(jpaEntity.getActive());
        admin.setRole(jpaEntity.getRole());
        admin.setCreatedAt(jpaEntity.getCreatedAt());
        admin.setLastLogin(jpaEntity.getLastLogin());

        return admin;
    }

    public AdminJpaEntity toJpaEntity(Admin domain) {
        if (domain == null) return null;

        return AdminJpaEntity.builder()
                .id(domain.getId())
                .username(domain.getUsername())
                .email(domain.getEmail())
                .password(domain.getPassword())
                .cpf(domain.getCpf())
                .phone(domain.getPhone())
                .address(domain.getAddress())
                .active(domain.getActive())
                .role(domain.getRole())
                .fullAccess(domain.getFullAccess())
                .createdAt(domain.getCreatedAt())
                .lastLogin(domain.getLastLogin())
                .build();
    }
}
