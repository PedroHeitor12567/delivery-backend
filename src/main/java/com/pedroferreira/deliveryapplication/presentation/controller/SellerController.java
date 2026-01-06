package com.pedroferreira.deliveryapplication.presentation.controller;

import com.pedroferreira.deliveryapplication.application.dto.requests.SellerApplicationRequest;
import com.pedroferreira.deliveryapplication.application.dto.response.OrderResponse;
import com.pedroferreira.deliveryapplication.application.dto.response.SellerResponse;
import com.pedroferreira.deliveryapplication.application.service.SellerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vendedores", description = "Gestão de vendedores e suas lojas")
public class SellerController {

    private final SellerService sellerService;

    @PostMapping("/apply")
    @Operation(
            summary = "Solcitar para ser vendedor",
            description = "Cliente envia solicitação para se tornar vendedor. Admin deve aprovar"
    )
    public ResponseEntity<String> applyToBeSeller(
            @Valid @RequestBody SellerApplicationRequest request
    ) {
        log.info("POST /api/sellers/apply - Cliente {} solicitando ser vendedor", request.getCustomerId());
        String message = sellerService.createSellerApplication(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar vendedor por ID"
    )
    public ResponseEntity<SellerResponse> getSeller(
            @Parameter(description = "ID do vendedor") @PathVariable Long id
    ) {
        log.info("GET /api/sellers/{} - Buscando vendedor", id);
        SellerResponse response = sellerService.getSellerById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/store/{storeId}")
    @Operation(
            summary = "Buscar vendedor por ID da loja"
    )
    public ResponseEntity<SellerResponse> getSellerByStore(
            @Parameter(description = "ID da loja") @PathVariable Long storeId
    ) {
        log.info("GET /api/sellers/store/{} - Buscando vendedor da loja", storeId);
        SellerResponse response = sellerService.getSellerByStoreId(storeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/orders")
    @Operation(
            summary = "Listar pedidos da loja do vendedor"
    )
    public ResponseEntity<List<OrderResponse>> getSellerOrders(
            @Parameter(description = "ID do vendedor") @PathVariable Long id
    ) {
        log.info("GET /api/sellers/{}/orders - Buscando pedidos do vendedor", id);
        List<OrderResponse> orders = sellerService.getSellerOrders(id);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}/orders/pending")
    @Operation(
            summary = "Listar pedidos pendentes (aguardando confirmação)"
    )
    public ResponseEntity<List<OrderResponse>> getPendingOrders(
            @Parameter(description = "ID do vendedor") @PathVariable Long id
    ) {
        log.info("GET /api/sellers/{}/orders/pending - Buscando pedidos pendentes", id);
        List<OrderResponse> orders = sellerService.getPendingOrders(id);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/orders/{orderId}/accept")
    @Operation(
            summary = "Aceitar pedido"
    )
    public ResponseEntity<OrderResponse> acceptOrder(
            @Parameter(description = "ID do pedido") @PathVariable Long orderId,
            @Parameter(description = "ID do vendedor") @RequestParam Long sellerId
    ) {
        log.info("PUT /api/sellers/orders/{}/accept - Vendedor {} aceitando pedido", orderId, sellerId);
        OrderResponse response = sellerService.acceptOrder(orderId, sellerId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/orders/{orderId}/refuse")
    @Operation(
            summary = "Recusar pedido"
    )
    public ResponseEntity<OrderResponse> refuseOrder(
            @Parameter(description = "ID do pedido") @PathVariable Long orderId,
            @Parameter(description = "ID do vendedor") @RequestParam Long sellerId,
            @Parameter(description = "Motivo da recusa")  @RequestParam String reason
    ) {
        log.info("PUT /api/sellers/orders/{}/refuse - Vendedor {} recusando pedido", orderId, sellerId);
        OrderResponse response = sellerService.refuseOrder(orderId, sellerId, reason);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/orders/{orderId}/ready")
    @Operation(
            summary = "Marcar pedido como pronto"
    )
    public ResponseEntity<OrderResponse> markOrderReady(
            @Parameter(description = "ID do pedido") @PathVariable Long orderId,
            @Parameter(description = "ID do vendedor") @RequestParam Long sellerId
    ) {
        log.info("PUT /api/sellers/orders/{}/ready - Marcando pedido como pronto",  orderId);
        OrderResponse response = sellerService.markOrderReady(orderId, sellerId);
        return ResponseEntity.ok(response);
    }
}
