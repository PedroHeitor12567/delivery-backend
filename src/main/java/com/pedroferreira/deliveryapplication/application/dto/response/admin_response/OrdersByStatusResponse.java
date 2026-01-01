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
public class OrdersByStatusResponse {
    private String status;
    private Long count;
    private BigDecimal totalValue;
}