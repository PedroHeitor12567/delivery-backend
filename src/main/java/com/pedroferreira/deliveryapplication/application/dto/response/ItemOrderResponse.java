package com.pedroferreira.deliveryapplication.application.dto.response;

import com.pedroferreira.deliveryapplication.domain.entity.ItemOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemOrderResponse {

    private Long id;
    private ProductSimpleResponse product;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal discount;
    private BigDecimal subtotal;
    private String observations;

    public static ItemOrderResponse fromEntity(ItemOrder item) {
        return ItemOrderResponse.builder()
                .id(item.getId())
                .product(ProductSimpleResponse.fromEntity(item.getProduct()))
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .discount(item.getDiscount())
                .subtotal(item.getSubtotal())
                .observations(item.getObservations())
                .build();
    }
}
