package com.pedroferreira.deliveryapplication.application.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

    @NotNull(message = "ID do cliente é obrigatório")
    private Long customerId;

    @NotNull(message = "ID da loja é obrigatório")
    private Long storeId;

    @NotBlank(message = "Endereço de entrega é obrigatório")
    private String deliveryAddress;

    @NotEmpty(message = "Pedido deve conter ao menos um item")
    private List<ItemOrderDTO> items;

    private String observations;
}


