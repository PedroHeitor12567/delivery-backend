package com.pedroferreira.deliveryapplication.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pedroferreira.deliveryapplication.domain.entity.Customer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Dados do cliente")
public class CustomerResponse {

    @Schema(description = "ID do cliente", example = "1")
    private Long id;

    @Schema(description = "Nome de usuário", example = "joaosilva")
    private String username;

    @Schema(description = "Email", example = "joao@email.com")
    private String email;

    @Schema(description = "CPF", example = "12345678901")
    private String cpf;

    @Schema(description = "Telefone", example = "11999999999")
    private String phone;

    @Schema(description = "Endereço", example = "Rua das Flores, 123")
    private String address;

    @Schema(description = "Pontos de fidelidade", example = "150")
    private Integer loyaltyPoints;

    @Schema(description = "Cliente ativo", example = "true")
    private Boolean active;

    public static CustomerResponse fromEntity(Customer customer) {
        if (customer == null) return null;

        return CustomerResponse.builder()
                .id(customer.getId())
                .username(customer.getUsername())
                .email(customer.getEmail())
                .cpf(customer.getCpf())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .loyaltyPoints(customer.getLoyaltyPoints())
                .active(customer.getActive())
                .build();
    }
}