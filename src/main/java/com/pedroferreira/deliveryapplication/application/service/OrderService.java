package com.pedroferreira.deliveryapplication.application.service;

import com.pedroferreira.deliveryapplication.application.dto.response.OrderResponse;
import com.pedroferreira.deliveryapplication.application.usecase.BusinessException;
import com.pedroferreira.deliveryapplication.application.usecase.ResourceNotFoundException;
import com.pedroferreira.deliveryapplication.domain.entity.Customer;
import com.pedroferreira.deliveryapplication.domain.entity.Order;
import com.pedroferreira.deliveryapplication.domain.enuns.StatusOrder;
import com.pedroferreira.deliveryapplication.domain.repository.CustomerRepository;
import com.pedroferreira.deliveryapplication.domain.repository.OrderRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRespository orderRepository;
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = findOrderById(id);
        return OrderResponse.fromDomain(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByCustomer(Long customerId) {
        Customer customer = findCustomerById(customerId);

        List<Order> orders = orderRepository.findByCustomerId(customerId);
        return orders.stream()
                .map(OrderResponse::fromDomain)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByStore(Long storeId) {
        List<Order> orders = orderRepository.findByStoreId(storeId);
        return orders.stream()
                .map(OrderResponse::fromDomain)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getActiveOrdersByStore(Long storeId) {
        List<StatusOrder> activeStatuses = List.of(
                StatusOrder.CREATED,
                StatusOrder.CONFIRMED,
                StatusOrder.READY,
                StatusOrder.LEFT_FOR_DELIVERY
        );

        List<Order> orders = orderRepository.findByStoreId(storeId).stream()
                .filter(order -> activeStatuses.contains(order.getStatus()))
                .collect(Collectors.toList());

        return orders.stream()
                .map(OrderResponse::fromDomain)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse cancelOrder(Long orderId, Long customerId, String reason) {
        Order order = findOrderById(orderId);
        Customer customer = findCustomerById(customerId);

        if (!order.getCustomer().getId().equals(customerId)) {
            throw new BusinessException("Este pedido não pertence a você");
        }

        if (!customer.isActive()) {
            throw new BusinessException("Cliente inativo não pode cancelar pedidos");
        }

        order.cancel(reason);
        Order updated = orderRepository.save(order);

        log.info("Pedido {} cancelado pelo cliente {}", orderId, customerId);
        return OrderResponse.fromDomain(updated);
    }

    @Transactional
    public OrderResponse exitForDelivery(Long orderId) {
        Order order = findOrderById(orderId);
        order.exitForDelivery();
        Order updated = orderRepository.save(order);

        log.info("Pedido {} saiu para entrega", orderId);
        return OrderResponse.fromDomain(updated);
    }

    @Transactional
    public OrderResponse deliverOrder(Long orderId) {
        Order order = findOrderById(orderId);
        order.deliver();
        Order updated = orderRepository.save(order);

        log.info("Pedido {} entregue com sucesso", orderId);
        return OrderResponse.fromDomain(updated);
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado: " + id));
    }

    private Customer findCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: " + id));
    }
}