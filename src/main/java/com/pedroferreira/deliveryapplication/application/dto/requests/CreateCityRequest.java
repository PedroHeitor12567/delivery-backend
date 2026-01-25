package com.pedroferreira.deliveryapplication.application.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCityRequest {

    @NotBlank(message = "Nome da cidade é obrigatório")
    private String name;

    @NotBlank(message = "Estado é obrigatório")
    private String state;
}
