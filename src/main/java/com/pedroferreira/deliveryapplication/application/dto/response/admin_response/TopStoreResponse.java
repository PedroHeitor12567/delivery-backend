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
public class TopStoreResponse {
    private Long storeId;
    private String storeName;
    private String category;
    private Integer totalOrders;
    private BigDecimal totalRevenue;
    private BigDecimal rating;
    private Integer totalRatings;
}