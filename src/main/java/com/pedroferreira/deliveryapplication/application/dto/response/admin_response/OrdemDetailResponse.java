package com.pedroferreira.deliveryapplication.application.dto.response.admin_response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdemDetailResponse {
    private Long orderId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String storeName;
    private String storeCategory;
    private String cityName;
    private String deliveryAddress;
    private BigDecimal totalAmount;
    private BigDecimal deliveryFee;
    private String status;
    private String createdAt;
    private String deliveredAt;
    private List<OrderItemDetail> items;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class OrderItemDetail {
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}

