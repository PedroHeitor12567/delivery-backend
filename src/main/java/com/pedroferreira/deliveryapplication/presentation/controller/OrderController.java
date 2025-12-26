package com.pedroferreira.deliveryapplication.presentation.controller;

import com.pedroferreira.deliveryapplication.application.dto.requests.CreateOrderRequest;
import com.pedroferreira.deliveryapplication.application.dto.response.OrderResponse;
import com.pedroferreira.deliveryapplication.application.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
        OrderResponse response = orderService.getOrderById(orderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomer(@PathVariable Long customerId) {
        List<OrderResponse> orders = orderService.getOrdersByCustomer(customerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByStore(@PathVariable Long storeId) {
        List<OrderResponse> orders = orderService.getOrdersByStore(storeId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/store/{storeId}/active")
    public ResponseEntity<List<OrderResponse>> getAtiveOrdersByStore(@PathVariable Long storeId) {
        List<OrderResponse> orders = orderService.getActiveOrdersByStore(storeId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/ready")
    public ResponseEntity<List<OrderResponse>> getReadyOrders() {
        List<OrderResponse> orders = orderService.getReadyOrders();
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{orderId}/confirm")
    public ResponseEntity<OrderResponse> confirmOrder(
            @PathVariable Long orderId,
            @RequestParam Long sellerId
    ) {
        OrderResponse response = orderService.confrimOrder(orderId, sellerId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{orderId}/refuse")
    public ResponseEntity<OrderResponse> refuseOrder(
            @PathVariable Long orderId,
            @RequestParam String reason
    ) {
        OrderResponse response = orderService.refuseOrder(orderId, reason);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{orderId}/ready")
    public ResponseEntity<OrderResponse> markOrderReady(@PathVariable Long orderId) {
        OrderResponse response = orderService.markOrderReady(orderId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{orderId}/exit-delivery")
    public ResponseEntity<OrderResponse> exitForDelivery(@PathVariable Long orderId) {
        OrderResponse response = orderService.exitForDelivery(orderId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<OrderResponse> deliverOrder(@PathVariable Long orderId) {
        OrderResponse response = orderService.deliverOrder(orderId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long orderId,
            @RequestParam Long customerId,
            @RequestParam String reason
    ) {
        OrderResponse response = orderService.cancelOrder(orderId, customerId, reason);
        return ResponseEntity.ok(response);
    }

}
