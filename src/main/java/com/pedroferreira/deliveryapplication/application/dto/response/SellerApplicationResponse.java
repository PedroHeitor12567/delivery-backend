package com.pedroferreira.deliveryapplication.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerApplicationResponse {
    private Long id;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String proposedStoreName;
    private String category;
    private String description;
    private String storeAddress;
    private String city;
    private String state;
    private String businessPhone;
    private String whatsapp;
    private String status;
    private LocalDateTime appliedAt;
    private String rejectionReason;
}
