package com.pedroferreira.deliveryapplication.application.dto.response.admin_response;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesByCityResponse {
    private Long cityId;
    private String state;
    private Integer totalStores;
    private Integer totalOrders;
    private BigDecimal totalRevenue;
    private BigDecimal platformRevenue;
    private BigDecimal averageOrderValue;
}