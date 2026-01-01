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


