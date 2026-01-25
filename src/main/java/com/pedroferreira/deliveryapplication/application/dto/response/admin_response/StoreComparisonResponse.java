package com.pedroferreira.deliveryapplication.application.dto.response.admin_response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreComparisonResponse {
    private List<StoreComparisonItem> stores;
    private String period;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class StoreComparisonItem {
    private Long storeId;
    private String storeName;
    private String cityName;
    private Integer totalOrders;
    private BigDecimal totalRevenue;
    private BigDecimal platformFee;
    private BigDecimal rating;
}