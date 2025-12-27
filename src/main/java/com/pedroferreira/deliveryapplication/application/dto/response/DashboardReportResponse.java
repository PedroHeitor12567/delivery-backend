package com.pedroferreira.deliveryapplication.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardReportResponse {
    private PeriodStats today;
    private PeriodStats thisWeek;
    private PeriodStats thisMonth;
    private List<TopStoreResponse> topStores;
    private List<OrdersByStatusResponse> ordersByStatus;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class PeriodStats {
    private Long totalOrders;
    private BigDecimal totalRevenue;
    private Long totalCustomers;
    private Long activeStores;
    private BigDecimal averageOrderValue;
    private Double cancellationRate;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class TopStoreResponse {
    private Long storeId;
    private String storeName;
    private String category;
    private Integer totalOrders;
    private BigDecimal totalRevenue;
    private BigDecimal rating;
    private Integer totalRatings;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class OrdersByStatusResponse {
    private String status;
    private Long count;
    private BigDecimal totalValue;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class RevenueByDayResponse {
    private LocalDate date;
    private Long totalOrders;
    private BigDecimal totalRevenue;
    private BigDecimal averageOrderValue;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class StorePerformanceResponse {
    private Long storeId;
    private String storeName;
    private String category;
    private Integer totalOrders;
    private Integer completedOrders;
    private BigDecimal totalRevenue;
    private BigDecimal rating;
    private Double completionRate;
    private Double cancellationRate;
    private BigDecimal averageOrderValue;
    private Integer averagePrepationTime;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class CustomerReportResponse {
    private Long customerId;
    private String username;
    private String email;
    private Integer totalOrders;
    private BigDecimal totalSpent;
    private Integer loyaltyPoints;
    private LocalDate lastOrderDate;
    private BigDecimal averageOrderValue;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class SalesByCategoryResponse {
    private String category;
    private Long totalOrders;
    private BigDecimal totalRevenue;
    private Integer activeStores;
    private BigDecimal averageOrderValue;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class DeliveryStatsResponse {
    private BigDecimal averageDistance;
    private BigDecimal averageDeliveryFee;
    private BigDecimal totalDeliveryRevenue;
    private BigDecimal minDistance;
    private BigDecimal maxDistance;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class SystemStatsResponse {
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