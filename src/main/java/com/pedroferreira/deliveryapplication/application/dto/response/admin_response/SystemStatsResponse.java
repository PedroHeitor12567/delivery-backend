package com.pedroferreira.deliveryapplication.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemStatsResponse {
    private Long totalUsers;
    private Long totalCustomers;
    private Long totalSellers;
    private Long totalAdmins;
    private Long totalStores;
    private Long activeStores;
    private Long totalProducts;
    private Long availableProducts;
    private Long totalOrders;
    private Long ordersToday;
}