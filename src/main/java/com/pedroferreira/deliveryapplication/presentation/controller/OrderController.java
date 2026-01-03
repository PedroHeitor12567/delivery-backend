package com.pedroferreira.deliveryapplication.presentation.controller;

import com.pedroferreira.deliveryapplication.application.dto.requests.CreateOrderRequest;
import com.pedroferreira.deliveryapplication.application.dto.response.OrderResponse;
import com.pedroferreira.deliveryapplication.application.service.OrderService;
import com.pedroferreira.deliveryapplication.application.usecase.CreateOrderUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Pedidos", description = "Gerenciamento de pedidos")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        log.info("POST /api/orders - Criando pedido para cliente: {}", request.getCustomerId());
        OrderResponse response = createOrderUseCase.execute(request);
        log.info("Pedido criado com sucesso - ID: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
        log.info("GET /api/orders/{} - Buscando pedido", id);
        OrderResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> getCustomersOrders(@PathVariable Long customerId) {
        log.info("GET /api/orders/customer/{} - Buscando pedidos do cliente", customerId);
        List<OrderResponse> orders = orderService.getOrdersByCustomer(customerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<OrderResponse>> getStoreOrders(@PathVariable Long storeId) {
        log.info("GET /api/orders/store/{} - Buscando pedidos da loja", storeId);
        List<OrderResponse> orders = orderService.getOrdersByStore(storeId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/store/{storeId}/active")
    public ResponseEntity<List<OrderResponse>> getActiveStoreOrders(@PathVariable Long storeId) {
        log.info("GET /api/orders/store/{}/active - Buscando pedidos ativos", storeId);
        List<OrderResponse> orders = orderService.getActiveOrdersByStore(storeId);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<OrderResponse> confirmOrder(
            @PathVariable Long id,
            @RequestParam Long sellerId
    ) {
        log.info("PUT /api/orders/{}/confirm - Confirmando pedido", id);
        OrderResponse response = orderService.confrimOrder(id, sellerId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/refuse")
    public ResponseEntity<OrderResponse> refuseOrder(
            @PathVariable Long id,
            @RequestParam String reason
    ) {
        log.info("PUT /api/orders/{}/refuse - Recusando pedido", id);
        OrderResponse response = orderService.refuseOrder(id, reason);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/ready")
    public ResponseEntity<OrderResponse> markOrderReady(@PathVariable Long id) {
        log.info("PUT /api/orders/{}/ready - Marcando pedido como pronto", id);
        OrderResponse response = orderService.markOrderReady(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/out-for-delivery")
    public ResponseEntity<OrderResponse> exitForDelivery(@PathVariable Long id) {
        log.info("PUT /api/orders/{}/out-for-delivery - Pedido saiu para entrega", id);
        OrderResponse response = orderService.exitForDelivery(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/deliver")
    public ResponseEntity<OrderResponse> deliverOrder(@PathVariable Long id) {
        log.info("PUT /api/orders/{}/deliver - Entregando pedido", id);
        OrderResponse response = orderService.deliverOrder(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long id,
            @RequestParam Long customerId,
            @RequestParam String reason
    ) {
        log.info("PUT /api/orders/{}/cancel - Cancelando pedido", id);
        OrderResponse response = orderService.cancelOrder(id, customerId, reason);
        return ResponseEntity.ok(response);
    }
}