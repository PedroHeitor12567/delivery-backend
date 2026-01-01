package com.pedroferreira.deliveryapplication.application.dto.response.admin_response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeriodStats {
    private Long totalOrders;
    private BigDecimal totalRevenue;
    private Long totalCustomers;
    private Long activeStores;
    private BigDecimal averageOrderValue;
    private Double cancellationRate;
}