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
public class DeliveryStatsResponse {
    private BigDecimal averageDistance;
    private BigDecimal averageDeliveryFee;
    private BigDecimal totalDeliveryRevenue;
    private BigDecimal minDistance;
    private BigDecimal maxDistance;
}