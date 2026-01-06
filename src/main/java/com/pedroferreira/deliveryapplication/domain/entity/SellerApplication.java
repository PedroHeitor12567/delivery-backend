package com.pedroferreira.deliveryapplication.domain.entity;

import com.pedroferreira.deliveryapplication.domain.enuns.ApplicationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerApplication {

    private Long id;
    private Customer customer;
    private String proposedStoreName;
    private String category;
    private String description;
    private String storeAddress;
    private String city;
    private String state;
    private String businessPhone;
    private String whatsapp;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;
    private LocalDateTime processedAt;
    private String rejectionReason;
    private Admin processedBy;

    public SellerApplication(Customer customer, String proposedStoreName, String category, String description, String storeAddress, String city, String state, String businessPhone, String whatsapp) {
        validateApplication(customer, proposedStoreName, category, storeAddress, city, state);

        this.customer = customer;
        this.proposedStoreName = proposedStoreName;
        this.category = category;
        this.description = description;
        this.storeAddress = storeAddress;
        this.city = city;
        this.state = state;
        this.businessPhone = businessPhone;
        this.whatsapp = whatsapp;
        this.status = ApplicationStatus.PENDING;
        this.appliedAt = LocalDateTime.now();
    }

    private void validateApplication(Customer customer, String storeName, String category, String address, String city, String state) {
        if (customer == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo");
        }
        if (!customer.isActive()) {
            throw new IllegalStateException("Cliente deve estar ativo para solicitar ser vendedor");
        }
        if (storeName == null || storeName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da loja é obrigatório");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria é obrigatória");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Endereço é obrigatório");
        }
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("Cidade é obrigatória");
        }
        if (state == null || state.trim().isEmpty()) {
            throw new IllegalArgumentException("Estado é obrigatório");
        }
    }

    public void approve(Admin admin) {
        if (this.status != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Apenas solicitações pendentes podem ser aprovadas");
        }
        this.status = ApplicationStatus.APPROVED;
        this.processedAt = LocalDateTime.now();
        this.processedBy = admin;
    }

    public void reject(Admin admin, String reason) {
        if (this.status != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Apenas solicitações pendentes podem ser rejeitadas");
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Motivo da rejeição é obrigatório");
        }
        this.status = ApplicationStatus.REJECTED;
        this.processedAt = LocalDateTime.now();
        this.processedBy = admin;
        this.rejectionReason = reason;
    }

    public boolean isPending() {
        return this.status == ApplicationStatus.PENDING;
    }

    public boolean isApproved() {
        return this.status == ApplicationStatus.APPROVED;
    }

    public boolean isRejected() {
        return this.status == ApplicationStatus.REJECTED;
    }
}
