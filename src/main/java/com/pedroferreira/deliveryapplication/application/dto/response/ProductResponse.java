package com.pedroferreira.deliveryapplication.application.dto.response;

import com.pedroferreira.deliveryapplication.domain.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Boolean available;
    private Integer preparationTime;
    private StoreSimpleResponse store;

    public static ProductResponse fromEntity(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .available(product.getAvailable())
                .preparationTime(product.getPreparationTime())
                .store(StoreSimpleResponse.fromEntity(product.getStore()))
                .build();
    }
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class ProductSimpleResponse {
    private Long id;
    private String name;
    private BigDecimal price;

    public static ProductSimpleResponse fromEntity(Product product) {
        return ProductSimpleResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
