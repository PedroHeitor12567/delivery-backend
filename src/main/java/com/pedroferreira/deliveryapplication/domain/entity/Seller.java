package com.pedroferreira.deliveryapplication.domain.entity;

import com.pedroferreira.deliveryapplication.domain.enuns.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Seller extends User {

    private Store store;

    @Builder
    public Seller(Long id, String username, String email, String password, String cpf, String phone, String address, Store store) {
        super(username, email, password, cpf, phone, address, UserRole.SELLER);
        this.setId(id);
        this.store = store;
    }

    @Override
    public UserRole getUserRole(){
        return UserRole.SELLER;
    }

    public void acceptOrder(Order order) {
        validateOrderBelongsToStore(order);
        order.confirm();
    }

    public void refuseOrder(Order order, String reason) {
        validateOrderBelongsToStore(order);
        order.refuse(reason);
    }

    public void markOrderReady(Order order) {
        validateOrderBelongsToStore(order);
        order.markReady();
    }

    private void validateOrderBelongsToStore(Order order) {
        if (!order.getStore().equals(this.store)) {
            throw new IllegalStateException("Pedido não pertence a esta loja");
        }
        if (store == null) {
            throw new IllegalStateException("Vendendor não possui loja associada");
        }
        if (order == null || order.getStore() == null) {
            throw new IllegalArgumentException("Pedido inválido");
        }
    }
}
