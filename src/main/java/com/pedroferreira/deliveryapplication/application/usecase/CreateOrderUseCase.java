package com.pedroferreira.deliveryapplication.application.usecase;

import com.pedroferreira.deliveryapplication.application.dto.requests.CreateOrderRequest;
import com.pedroferreira.deliveryapplication.application.dto.response.OrderResponse;
import com.pedroferreira.deliveryapplication.domain.entity.*;
import com.pedroferreira.deliveryapplication.domain.repository.CustomerRepository;
import com.pedroferreira.deliveryapplication.domain.repository.OrderRespository;
import com.pedroferreira.deliveryapplication.domain.repository.ProductRepository;
import com.pedroferreira.deliveryapplication.domain.repository.StoreRespository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateOrderUseCase {

    private final OrderRespository orderRepository;
    private final CustomerRepository customerRepository;
    private final StoreRespository storeRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderResponse execute(CreateOrderRequest request) {
        log.info("Iniciando criação de pedido - Cliente: {}, Loja: {}",
                request.getCustomerId(), request.getStoreId());

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: " + request.getCustomerId()));

        if (!customer.isActive()) {
            throw new BusinessException("Cliente está inativo");
        }

        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada: " + request.getStoreId()));

        if (!store.isOpenNow()) {
            throw new BusinessException("Loja está fechada no momento");
        }

        Order order = new Order(
                customer,
                store,
                request.getDeliveryAddress(),
                request.getDeliveyDistanceKm(),
                request.getObservations()
        );

        request.getItems().forEach(itemDto -> {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + itemDto.getProductId()));

            if (!Boolean.TRUE.equals(product.getAvailable())) {
                throw new BusinessException("Produto indiponível: " + product.getName());
            }

            ItemOrder item = new ItemOrder(
                    product,
                    itemDto.getQuantity(),
                    product.getPrice(),
                    itemDto.getObservations()
            );

            order.addItem(item);
        });

        order.validate();

        Order savedOrder = orderRepository.save(order);

        customer.addOrder(savedOrder);
        customerRepository.save(customer);

        log.info("Pedido criado com sucesso - ID: {}, Total: R$ {}",
                savedOrder.getId(), savedOrder.getTotalAmount());

        return OrderResponse.fromDomain(savedOrder);
    }
}

