package com.pedroferreira.deliveryapplication.presentation.controller;

import com.pedroferreira.deliveryapplication.application.dto.requests.CreateOrderRequest;
import com.pedroferreira.deliveryapplication.application.dto.response.OrderResponse;
import com.pedroferreira.deliveryapplication.application.service.OrderService;
import com.pedroferreira.deliveryapplication.application.usecase.CreateOrderUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        log.info("POST /api/orders - Criando pedido para clientes: {}", request.getCustomerId());
        OrderResponse response = createOrderUseCase.execute(request);
        log.info("Pedido criado com sucesso - ID: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
        log.info("GET /api/orders/{} - Buscando pedido", id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> getCustomersOrders(@PathVariable Long customerId) {
        log.info("GET /api/orders/customer/{} - Buscando pedidos do cliente", customerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<OrderResponse>> getStoreOrders(@PathVariable Long storeId) {
        log.info("GET /api/orders/store/{} - Buscando pedidos da loja", storeId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<OrderResponse> confirmOrder(
            @PathVariable Long id,
            @RequestParam Long sellerId
    ) {
        log.info("PUT /api/orders/{}/confirm - Confirmando pedido", id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/ready")
    public ResponseEntity<OrderResponse> markOrderReady(@PathVariable Long id) {
        log.info("PUT /api/orders/{}/ready - Marcando pedido como pronto", id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/deliver")
    public ResponseEntity<OrderResponse> deliverOrder(@PathVariable Long id) {
        log.info("PUT /api/orders/{}/deliver - Entregando pedido", id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long id,
            @RequestParam Long customerId,
            @RequestParam String reason
    ) {
        log.info("PUT /api/orders/{}/cancel - Cancelando pedido", id);
        return ResponseEntity.ok().build();
    }

}
