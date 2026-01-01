package com.pedroferreira.deliveryapplication.infrastructure.repository;

import com.pedroferreira.deliveryapplication.domain.entity.Order;
import com.pedroferreira.deliveryapplication.domain.enuns.StatusOrder;
import com.pedroferreira.deliveryapplication.domain.repository.OrderRespository;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.CustomerJpaEntity;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.OrderJpaEntity;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.ProductJpaEntity;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.StoreJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRespository {

    private final OrderJpaRepositorySpring jpaRepository;
    private final CustomerJpaRepositoryString customerJpaRepository;
    private final StoreJpaRepositorySpring storeJpaRepository;
    private final ProductJpaRepositorySpring productJpaRepository;

    @Override
    public Order save(Order order) {
        OrderJpaEntity jpaEntity = OrderJpaEntity.fromDomain(order);

        if (order.getCustomer() != null && order.getCustomer().getId() != null) {
            CustomerJpaEntity customer = customerJpaRepository.findById(order.getCustomer().getId())
                    .orElseThrow(() -> new RuntimeException("Customer not found: " + order.getCustomer().getId()));
            jpaEntity.setCustomer(customer);
        }

        if (order.getStore() != null && order.getStore().getId() != null) {
            StoreJpaEntity store = storeJpaRepository.findById(order.getStore().getId())
                    .orElseThrow(() -> new RuntimeException("Store not found: " + order.getStore().getId()));
            jpaEntity.setStore(store);
        }

        if (jpaEntity.getItems() != null) {
            jpaEntity.getItems().forEach(item -> {
                if (order.getItems() != null) {
                    order.getItems().stream()
                            .filter(domainItem -> domainItem.getProduct() != null)
                            .findFirst()
                            .ifPresent(domainItem -> {
                                Long productId = domainItem.getProduct().getId();
                                ProductJpaEntity product = productJpaRepository.findById(productId)
                                        .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
                                item.setProduct(product);
                            });
                }
            });
        }
        OrderJpaEntity saved = jpaRepository.save(jpaEntity);

        return saved.toDomain();
    }

    @Override
    public Optional<Order> findById(Long id) {
        return jpaRepository.findById(id)
                .map(OrderJpaEntity::toDomain);
    }

    @Override
    public List<Order> findAll() {
        return jpaRepository.findAll().stream()
                .map(OrderJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByCustomerId(Long customerId) {
        return jpaRepository.findByCustomerId(customerId).stream()
                .map(OrderJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByStoreId(Long storeId) {
        return jpaRepository.findByStoreId(storeId).stream()
                .map(OrderJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByStatus(StatusOrder status) {
        return jpaRepository.findByStatus(status).stream()
                .map(OrderJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByCustomerIdAndStatus(Long customerId, StatusOrder status) {
        return List.of();
    }

    @Override
    public List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end) {
        return jpaRepository.findByCreatedAtBetween(start, end).stream()
                .map(OrderJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Long countByCreatedAtAfter(LocalDateTime date) {
        return jpaRepository.countByCreatedAtAfter(date);
    }

    @Override
    public List<Order> findByStoreIdAndStatusIn(Long storeId, List<StatusOrder> statuses) {
        return List.of();
    }

    @Override
    public List<Order> findByCustomerIdOrderByCreatedAtDesc(Long customerId) {
        return List.of();
    }

    @Override
    public List<Order> findByStatusOrderByCreatedAtAsc(StatusOrder status) {
        return List.of();
    }

    @Override
    public void delete(Order order) {
        if (order.getId() != null) {
            jpaRepository.deleteById(order.getId());
        }
    }

    @Override
    public Long count() {
        return jpaRepository.count();
    }

    @Override
    public List<Order> findByStoreIdAndStatus(Long id, StatusOrder statusOrder) {
        return List.of();
    }
}

interface OrderJpaRepositorySpring extends JpaRepository<OrderJpaEntity, Long> {
    List<OrderJpaEntity> findByCustomerId(Long customerId);
    List<OrderJpaEntity> findByStoreId(Long storeId);
    List<OrderJpaEntity> findByStatus(StatusOrder status);
    List<OrderJpaEntity> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    Long countByCreatedAtAfter(LocalDateTime createdAt);

    Long customer(CustomerJpaEntity customer);
}
