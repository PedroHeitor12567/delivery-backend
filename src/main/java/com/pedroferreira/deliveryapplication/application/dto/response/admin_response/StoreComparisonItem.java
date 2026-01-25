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
public class StoreComparisonItem {
    private Long storeId;
    private String storeName;
    private Long cityId;
    private String cityName;
    private String state;
    private Integer totalOrders;
    private BigDecimal totalRevenue;
    private BigDecimal platformFee;
    private BigDecimal rating;
}
