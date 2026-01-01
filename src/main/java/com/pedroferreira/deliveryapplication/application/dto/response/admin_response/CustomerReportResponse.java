package com.pedroferreira.deliveryapplication.application.dto.response.admin_response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerReportResponse {
    private Long customerId;
    private String username;
    private String email;
    private Integer totalOrders;
    private BigDecimal totalSpent;
    private Integer loyaltyPoints;
    private LocalDate lastOrderDate;
    private BigDecimal averageOrderValue;
}