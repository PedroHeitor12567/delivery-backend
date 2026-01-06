package com.pedroferreira.deliveryapplication.application.dto.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApproveSellerRequest {

    @NotNull(message = "ID do admin é obrigatório")
    private Long adminId;

    @NotNull(message = "Dados da loja são obrigatórios")
    @Valid
    private CreateStoreRequest storeData;

    @NotNull(message = "Taxa de entrega base é obrigatória")
    @DecimalMin(value = "0.0", message = "Taxa base deve ser >=0")
    private BigDecimal baseDeliveryFee;

    @NotNull(message = "Taxa por KM é obrigatória")
    @DecimalMin(value = "0.0", message = "Taxa por KM deve ser >= 0")
    private BigDecimal deliveryFeePerKm;

    @NotNull(message = "Pedido mínimo é obrigatório")
    @DecimalMin(value = "10.0", message = "Pedido mínimo deve ser >= R$ 10")
    private BigDecimal minimumOrder;

    private List<CreateProductRequest> initialProducts;

    private String observations;
}
