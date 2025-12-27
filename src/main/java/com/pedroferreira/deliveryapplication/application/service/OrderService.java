package com.pedroferreira.deliveryapplication.application.service;

import com.pedroferreira.deliveryapplication.application.dto.requests.CreateOrderRequest;
import com.pedroferreira.deliveryapplication.application.dto.response.OrderResponse;
import com.pedroferreira.deliveryapplication.domain.entity.*;
import com.pedroferreira.deliveryapplication.domain.enuns.EventRequest;
import com.pedroferreira.deliveryapplication.domain.enuns.StatusOrder;
import com.pedroferreira.deliveryapplication.domain.enuns.UserRole;
import com.pedroferreira.deliveryapplication.infrastructure.repository.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        if (!customer.isActive()) {
            throw new IllegalStateException("Cliente inativo");
        }

        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("Loja não encontrada"));

        if (!store.isOpenNow()) {
            throw new IllegalStateException("Loja está fechada no momento");
        }

        BigDecimal deliveryFee = store.calculateDeliveryFee(request.getDeliveyDistanceKm());

        Order order = Order.builder()
                .customer(customer)
                .store(store)
                .deliveryAddress(request.getDeliveryAddress())
                .deliveryDistanceKm(BigDecimal.valueOf(request.getDeliveyDistanceKm()))
                .deliveryFee(deliveryFee)
                .discount(BigDecimal.ZERO)
                .observations(request.getObservations())
                .build();

        request.getItems().forEach(itemDto -> {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Produto não encontrado: " + itemDto.getProductId()
                    ));

            ItemOrder item = ItemOrder.builder()
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .unitPrice(product.getPrice())
                    .discount(BigDecimal.ZERO)
                    .observations(itemDto.getObservations())
                    .build();

            order.addItem(item);
        });

        order.validate();

        Order saveOrder = orderRepository.save(order);
        customer.addOrder(saveOrder);

        return OrderResponse.fromEntity(saveOrder);
    }

    @Transactional
    public OrderResponse confrimOrder(Long orderId, Long sellerId) {
        Order order = findByOrderId(orderId);

        order.execute(EventRequest.CONFIRM, UserRole.SELLER);

        Order updateOrder = orderRepository.save(order);
        return OrderResponse.fromEntity(updateOrder);
    }

    @Transactional
    public OrderResponse refuseOrder(Long orderId, String reason) {
        Order order = findByOrderId(orderId);

        order.setCancellationReason(reason);
        order.execute(EventRequest.REFUSE, UserRole.SELLER);

        Order updateOrder = orderRepository.save(order);
        return OrderResponse.fromEntity(updateOrder);
    }

    @Transactional
    public OrderResponse markOrderReady(Long orderId) {
        Order order = findByOrderId(orderId);

        order.execute(EventRequest.MARK_POINT, UserRole.SELLER);

        Order updateOrder = orderRepository.save(order);
        return OrderResponse.fromEntity(updateOrder);
    }

    @Transactional
    public OrderResponse exitForDelivery(Long orderId) {
        Order order = findByOrderId(orderId);

        order.execute(EventRequest.EXIT_FOR_DELIVERY, UserRole.SYSTEM);

        Order updateOrder = orderRepository.save(order);
        return OrderResponse.fromEntity(updateOrder);
    }

    @Transactional
    public OrderResponse deliverOrder(Long orderId) {
        Order order = findByOrderId(orderId);

        order.execute(EventRequest.DELIVER, UserRole.SYSTEM);

        Store store = order.getStore();
        store.incrementSales();
        storeRepository.save(store);

        Order updateOrder = orderRepository.save(order);
        return OrderResponse.fromEntity(updateOrder);
    }

    @Transactional
    public OrderResponse cancelOrder(Long orderId, Long customerId, String reason) {
        Order order = findByOrderId(orderId);

        if (!order.getCustomer().getId().equals(customerId)) {
            throw new IllegalArgumentException("Pedido não pertence ao cliente");
        }

        if (!order.canBeCanceled()) {
            throw new IllegalStateException("Pedido não pode ser cancelado nesse momento");
        }

        order.cancel(reason);

        Order updateOrder = orderRepository.save(order);
        return OrderResponse.fromEntity(updateOrder);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        Order order = findByOrderId(orderId);
        return OrderResponse.fromEntity(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByStore(Long storeId) {
        return orderRepository.findByStoreId(storeId)
                .stream()
                .map(OrderResponse::fromEntity)
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

        return orderRepository.findByStoreId(storeId)
                .stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getReadyOrders() {
        return orderRepository.findByStatusOrderByCreatedAtAsc(StatusOrder.READY)
                .stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
    }

    private Order findByOrderId(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
    }
}
