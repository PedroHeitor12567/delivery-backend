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
public class PlatformRevenueResponse {
    private BigDecimal totalSalesValue;
    private BigDecimal platformFee;
    private BigDecimal sellersRevenue;
    private Integer totalCompletedOrders;
    private BigDecimal averageFeePerOrder;
}
