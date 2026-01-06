package com.pedroferreira.deliveryapplication.application.service;

import com.pedroferreira.deliveryapplication.application.dto.requests.SellerApplicationRequest;
import com.pedroferreira.deliveryapplication.application.dto.response.OrderResponse;
import com.pedroferreira.deliveryapplication.application.dto.response.SellerResponse;
import com.pedroferreira.deliveryapplication.application.usecase.BusinessException;
import com.pedroferreira.deliveryapplication.application.usecase.ResourceNotFoundException;
import com.pedroferreira.deliveryapplication.domain.entity.Customer;
import com.pedroferreira.deliveryapplication.domain.entity.Order;
import com.pedroferreira.deliveryapplication.domain.entity.Seller;
import com.pedroferreira.deliveryapplication.domain.enuns.StatusOrder;
import com.pedroferreira.deliveryapplication.domain.repository.CustomerRepository;
import com.pedroferreira.deliveryapplication.domain.repository.OrderRespository;
import com.pedroferreira.deliveryapplication.domain.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SellerService {

    private final SellerRepository sellerRepository;
    private final CustomerRepository customerRepository;
    private final OrderRespository orderRespository;

    @Transactional
    public String createSellerApplication(SellerApplicationRequest request){
        log.info("Cliente {} solicitando ser vendedor", request.getCustomerId());

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        if (!"SIM".equalsIgnoreCase(request.getTermsAccepted())) {
            throw new BusinessException("Você deve aceitar os termos para se tornar vendedor. " + "Confirme que aceita a taxa de 4% sobre cada venda.");
        }

        log.info("Solicitação criada. Cliente deve aguardar aprovação do Admin.");

        return "Solicitação enviada com sucesso! " +
                "Você receberá um contato do nosso time em até 48 horas. " +
                "Caso prefira, entre em contato pelo WhatsApp: (11) 99999-0000";
    }

    @Transactional(readOnly = true)
    public SellerResponse getSellerById(Long id) {
        Seller seller = findSellerById(id);
        return SellerResponse.fromEntity(seller);
    }

    @Transactional(readOnly = true)
    public SellerResponse getSellerByStoreId(Long storeId) {
        Seller seller = sellerRepository.findByStoreId(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendedor não encontrado para esta loja"));
        return SellerResponse.fromEntity(seller);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getSellerOrders(Long sellerId) {
        Seller seller = findSellerById(sellerId);

        if (seller.getStore() == null) {
            throw new BusinessException("Vendedor não possui loja associada");
        }

        List<Order> orders = orderRespository.findByStoreId(seller.getStore().getId());
        return orders.stream()
                .map(OrderResponse::fromDomain)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getPendingOrders(Long sellerId) {
        Seller seller = findSellerById(sellerId);

        if (seller.getActive() == null) {
            throw new BusinessException("Vendedor não possui loja associada");
        }

        List<Order> orders = orderRespository.findByStoreIdAndStatus(
                seller.getStore().getId(),
                StatusOrder.CREATED
        );

        return orders.stream()
                .map(OrderResponse::fromDomain)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse acceptOrder(Long orderId, Long sellerId) {
        Seller seller = findSellerById(sellerId);
        Order order = findOrderById(orderId);

        if (!order.getStore().getId().equals(seller.getStore().getId())) {
            throw new BusinessException("Este pedido não pertence à sua loja");
        }

        seller.acceptOrder(order);
        Order updated = orderRespository.save(order);

        log.info("Pedido {} aceito pelo vendedor {}", orderId, sellerId);
        return OrderResponse.fromDomain(updated);
    }

    @Transactional
    public OrderResponse refuseOrder(Long orderId, Long sellerId, String reason) {
        Seller seller = findSellerById(sellerId);
        Order order = findOrderById(orderId);

        if (!order.getStore().getId().equals(seller.getStore().getId())) {
            throw new BusinessException("Este pedido não pertence à sua loja");
        }

        seller.refuseOrder(order, reason);
        Order updated = orderRespository.save(order);

        log.info("Pedido {} recusado pelo vendedor {}. Motivo: {}", orderId, sellerId, reason);
        return OrderResponse.fromDomain(updated);
    }

    @Transactional
    public OrderResponse markOrderReady(Long orderId, Long sellerId) {
        Seller seller = findSellerById(sellerId);
        Order order = findOrderById(orderId);

        if (!order.getStore().getId().equals(seller.getStore().getId())) {
            throw new BusinessException("Este pedido não pertence à sua loja");
        }

        seller.markOrderReady(order);
        Order updated = orderRespository.save(order);

        log.info("Pedido {} marcado como pronto pelo vendedor {}", orderId, sellerId);
        return OrderResponse.fromDomain(updated);
    }

    private Seller findSellerById(Long id) {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendedor não encontrado: " + id));

    }

    private Order findOrderById(Long id) {
        return orderRespository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado: " + id));
    }
}
