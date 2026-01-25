package com.pedroferreira.deliveryapplication.application.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAddressRequest {

    @NotNull(message = "ID do cliente é obrigatório")
    private Long customerId;

    @NotNull(message = "ID da cidade é obrigatório")
    private Long cityId;

    @NotBlank(message = "Rua é obrigatória")
    private String street;

    @NotBlank(message = "Número é obrigatório")
    private String number;

    private String complement;

    @NotBlank(message = "Bairro é obrigatório")
    private String neighborhood;

    @NotBlank(message = "CEP é obrigatório")
    private String zipCode;

    private String reference;

    private Boolean isDefault;
}
