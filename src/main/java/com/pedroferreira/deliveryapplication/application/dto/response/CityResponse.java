package com.pedroferreira.deliveryapplication.application.dto.response;

import com.pedroferreira.deliveryapplication.domain.entity.City;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityResponse {
    private Long id;
    private String name;
    private String state;
    private Boolean active;

    public static CityResponse fromEntity(City city) {
        if (city == null) return null;

        return CityResponse.builder()
                .id(city.getId())
                .name(city.getName())
                .state(city.getState())
                .active(city.getActive())
                .build();
    }
}
