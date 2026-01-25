package com.pedroferreira.deliveryapplication.application.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerApplicationRequest {

    @NotNull(message = "ID do cliente é obrigatório")
    private Long customerId;

    @NotBlank(message = "Nome da loja proposta é obrigatório")
    private String proposedStoreName;

    @NotBlank(message = "Categoria é obrigatória")
    private String category;

    @NotBlank(message = "Descrição é obrigatória")
    private String description;

    @NotBlank(message = "Endereço da loja é obrigatório")
    private String storeAddress;

    @NotBlank(message = "ID da cidade é obrigatório")
    private Long cityId;

    @NotBlank(message = "Telefone comercial é obrigatório")
    private String businessPhone;

    private String whatsapp;

    @NotBlank(message = "Você deve aceitar os termos")
    private String termsAccepted;
}