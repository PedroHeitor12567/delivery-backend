package com.pedroferreira.deliveryapplication.application.dto.response.admin_response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreComparisonResponse {
    private List<StoreComparisonItem> stores;
    private String period;
}

