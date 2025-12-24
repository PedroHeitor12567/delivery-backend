package com.pedroferreira.deliveryapplication.application.dto.response;

import com.pedroferreira.deliveryapplication.domain.entity.Order;
import com.pedroferreira.deliveryapplication.domain.enuns.StatusOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private CustomerSimpleResponse customer;
    private StoreSimpleResponse store;
    private List<ItemOrderResponse> items;
    private StatusOrder status;
    private String deliveryAddress;
    private BigDecimal deliveryFee;
    private BigDecimal discount;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime readyAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime canceledAt;
    private String observations;
    private String cancellationReason;

    public static OrderResponse fromEntity(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customer(CustomerSimpleResponse.fromEntity(order.getCustomer()))
                .store(StoreSimpleResponse.fromEntity(order.getStore()))
                .items(order.getItems().stream()
                        .map(ItemOrderResponse::fromEntity)
                        .collect(Collectors.toList()))
                .status(order.getStatus())
                .deliveryAddress(order.getDeliveryAddress())
                .deliveryFee(order.getDeliveryFee())
                .discount(order.getDiscount())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .confirmedAt(order.getConfirmedAt())
                .readyAt(order.getReadyAt())
                .deliveredAt(order.getDeliveredAt())
                .canceledAt(order.getCanceledAt())
                .observations(order.getObservations())
                .cancellationReason(order.getCancellationReason())
                .build();
    }
}
