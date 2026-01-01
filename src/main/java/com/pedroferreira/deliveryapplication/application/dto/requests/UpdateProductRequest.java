package com.pedroferreira.deliveryapplication.application.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {
    private String name;
    private String description;
    private java.math.BigDecimal price;
    private String imageUrl;
    private Boolean available;
    private Integer preparationTime;
}
