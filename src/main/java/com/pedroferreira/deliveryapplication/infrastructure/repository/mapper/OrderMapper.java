package com.pedroferreira.deliveryapplication.infrastructure.repository.mapper;

import com.pedroferreira.deliveryapplication.domain.entity.ItemOrder;
import com.pedroferreira.deliveryapplication.domain.entity.Order;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.ItemOrderJpaEntity;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.OrderJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    private final CustomerMapper customerMapper;
    private final StoreMapper storeMapper;
    private final ItemOrderMapper itemOrderMapper;

    public OrderMapper(
            CustomerMapper customerMapper,
            StoreMapper storeMapper,
            ItemOrderMapper itemOrderMapper
    ) {
        this.customerMapper = customerMapper;
        this.storeMapper = storeMapper;
        this.itemOrderMapper = itemOrderMapper;
    }

    public Order toDomain(OrderJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;

        Order order = Order.builder()
                .id(jpaEntity.getId())
                .status(jpaEntity.getStatus())
                .deliveryAddress(jpaEntity.getDeliveryAddress())
                .deliveryDistanceKm(jpaEntity.getDeliveryDistanceKm())
                .deliveryFee(jpaEntity.getDeliveryFee())
                .discount(jpaEntity.getDiscount())
                .totalAmount(jpaEntity.getTotalAmount())
                .createdAt(jpaEntity.getCreatedAt())
                .observations(jpaEntity.getObservations())
                .cancellationReason(jpaEntity.getCancellationReason())
                .build();

        order.setConfirmedAt(jpaEntity.getConfirmedAt());
        order.setReadyAt(jpaEntity.getReadyAt());
        order.setDeliveredAt(jpaEntity.getDeliveredAt());
        order.setCanceledAt(jpaEntity.getCanceledAt());

        if (jpaEntity.getCustomer() != null) {
            order.setCustomer(
                    customerMapper.toDomain(jpaEntity.getCustomer())
            );
        }

        if (jpaEntity.getStore() != null) {
            order.setStore(
                    storeMapper.toDomain(jpaEntity.getStore())
            );
        }

        if (jpaEntity.getItems() != null && !jpaEntity.getItems().isEmpty()) {
            List<ItemOrder> items = jpaEntity.getItems().stream()
                    .map(itemOrderMapper::toDomain)
                    .collect(Collectors.toList());
            order.setItems(items);
        }

        return order;
    }

    public OrderJpaEntity toJpaEntity(Order domain) {
        if (domain == null) return null;

        OrderJpaEntity jpaEntity = OrderJpaEntity.builder()
                .id(domain.getId())
                .status(domain.getStatus())
                .deliveryAddress(domain.getDeliveryAddress())
                .deliveryDistanceKm(domain.getDeliveryDistanceKm())
                .deliveryFee(domain.getDeliveryFee())
                .discount(domain.getDiscount())
                .totalAmount(domain.getTotalAmount())
                .createdAt(domain.getCreatedAt())
                .confirmedAt(domain.getConfirmedAt())
                .readyAt(domain.getReadyAt())
                .deliveredAt(domain.getDeliveredAt())
                .canceledAt(domain.getCanceledAt())
                .observations(domain.getObservations())
                .cancellationReason(domain.getCancellationReason())
                .customer(
                        domain.getCustomer() != null
                                ? customerMapper.toJpaEntity(domain.getCustomer())
                                : null
                )
                .store(
                        domain.getStore() != null
                                ? storeMapper.toJpaEntity(domain.getStore())
                                : null
                )
                .build();

        if (domain.getItems() != null && !domain.getItems().isEmpty()) {
            List<ItemOrderJpaEntity> jpaItems = domain.getItems().stream()
                    .map(item -> {
                        ItemOrderJpaEntity jpaItem =
                                itemOrderMapper.toJpaEntity(item);
                        jpaItem.setOrder(jpaEntity); // relacionamento dono
                        return jpaItem;
                    })
                    .collect(Collectors.toList());

            jpaEntity.setItems(jpaItems);
        }

        return jpaEntity;
    }
}
