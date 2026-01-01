package com.pedroferreira.deliveryapplication.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorePerfoRmanceResponse {
    private Long storeId;
    private String storeName;
    private String category;
    private Integer totalOrders;
    private Integer completedOrders;
    private Integer canceledOrders;
    private BigDecimal totalRevenue;
    private BigDecimal rating;
    private Double completionRate;
    private Double cancellationRate;
    private BigDecimal averageOrderValue;
    private Integer averagePrepationTime;
}
