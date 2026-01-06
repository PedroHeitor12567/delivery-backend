package com.pedroferreira.deliveryapplication.infrastructure.repository.mapper;

import com.pedroferreira.deliveryapplication.domain.entity.Admin;
import com.pedroferreira.deliveryapplication.domain.entity.Customer;
import com.pedroferreira.deliveryapplication.domain.entity.SellerApplication;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.AdminJpaEntity;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.CustomerJpaEntity;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.SellerApplicationJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class SellerApplicationMapper {
    private final CustomerMapper customerMapper;
    private final AdminMapper adminMapper;

    public SellerApplicationMapper(CustomerMapper customerMapper, AdminMapper adminMapper) {
        this.customerMapper = customerMapper;
        this.adminMapper = adminMapper;
    }

    public SellerApplication toDomain(SellerApplicationJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;

        Customer customer = customerMapper.toDomain(jpaEntity.getCustomer());
        Admin processedBy = jpaEntity.getProcessedBy() != null ?
                adminMapper.toDomain(jpaEntity.getProcessedBy()) : null;

        return SellerApplication.builder()
                .id(jpaEntity.getId())
                .customer(customer)
                .proposedStoreName(jpaEntity.getProposedStoreName())
                .category(jpaEntity.getCategory())
                .description(jpaEntity.getDescription())
                .storeAddress(jpaEntity.getStoreAddress())
                .city(jpaEntity.getCity())
                .state(jpaEntity.getState())
                .businessPhone(jpaEntity.getBusinessPhone())
                .whatsapp(jpaEntity.getWhatsapp())
                .status(jpaEntity.getStatus())
                .appliedAt(jpaEntity.getAppliedAt())
                .processedAt(jpaEntity.getProcessedAt())
                .rejectionReason(jpaEntity.getRejectionReason())
                .processedBy(processedBy)
                .build();
    }

    public SellerApplicationJpaEntity toJpaEntity(SellerApplication domain) {
        if (domain == null) return null;

        CustomerJpaEntity customerJpa = customerMapper.toJpaEntity(domain.getCustomer());
        AdminJpaEntity processedByJpa = domain.getProcessedBy() != null ?
        adminMapper.toJpaEntity(domain.getProcessedBy()) : null;

        return SellerApplicationJpaEntity.builder()
                .id(domain.getId())
                .customer(customerJpa)
                .proposedStoreName(domain.getProposedStoreName())
                .category(domain.getCategory())
                .description(domain.getDescription())
                .storeAddress(domain.getStoreAddress())
                .city(domain.getCity())
                .state(domain.getState())
                .businessPhone(domain.getBusinessPhone())
                .whatsapp(domain.getWhatsapp())
                .status(domain.getStatus())
                .appliedAt(domain.getAppliedAt())
                .processedAt(domain.getProcessedAt())
                .rejectionReason(domain.getRejectionReason())
                .processedBy(processedByJpa)
                .build();
    }
}
