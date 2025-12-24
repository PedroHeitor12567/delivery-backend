package com.pedroferreira.deliveryapplication.application.dto.response;

import com.pedroferreira.deliveryapplication.domain.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponse {

    private Long id;
    private String name;
    private String description;
    private String city;
    private String state;
    private String phone;
    private String email;
    private String address;
    private String category;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private BigDecimal deliveryFee;
    private BigDecimal minimumOrder;
    private Boolean active;
    private Boolean open;
    private BigDecimal rating;
    private Integer totalRatings;
    private Integer totalSales;

    public static StoreResponse fromEntity(Store store) {
        return StoreResponse.builder()
                .id(store.getId())
                .name(store.getName())
                .description(store.getDescription())
                .city(store.getCity())
                .state(store.getState())
                .phone(store.getPhone())
                .email(store.getEmail())
                .address(store.getAddress())
                .category(store.getCategory())
                .openingTime(store.getOpeningTime())
                .openingTime(store.getClosingTime())
                .deliveryFee(store.getDeliveryFee())
                .minimumOrder(store.getMinimumOrder())
                .active(store.getActive())
                .rating(store.getRating())
                .totalRatings(store.getTotalRatings())
                .totalSales(store.getTotalSales())
                .build();
    }
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class StoreSimpleResponse {

    private Long id;
    private String name;
    private String category;

    public static StoreSimpleResponse fromEntity(Store store) {
        return StoreSimpleResponse.builder()
                .id(store.getId())
                .name(store.getName())
                .category(store.getCategory())
                .build();
    }
}
