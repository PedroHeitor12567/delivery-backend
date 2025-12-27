package com.pedroferreira.deliveryapplication.application.dto.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateStoreRequest {

    @NotBlank(message = "Nome da loja é obrigatório")
    private String name;

    private String description;

    @NotBlank(message = "Cidade é obrigatória")
    private String city;

    @NotBlank(message = "Estado é obrigatório")
    private String state;

    @NotBlank(message = "Telefone é obrigatório")
    private String phone;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Endereço é obrigatório")
    private String address;

    @NotBlank(message = "Categoria é obrigatória")
    private String category;

    @NotNull(message = "Taxa de entrega por KM é obrigatória")
    @DecimalMin(value = "0.0", message = "Taxa por KM deve ser maior ou igual a zero")
    private BigDecimal deliveryFeePerKm;

    @NotNull(message = "Taxa de entrega é obrigatória")
    @DecimalMin(value = "0.0", message = "Taxa base deve ser maior ou igual a zero")
    private BigDecimal baseDeliveryFee;

    @NotNull(message = "Pedido mínimo é obrigatório")
    private BigDecimal minimumOrder;

    private String openingTime;
    private String closingTime;
}
