package com.pedroferreira.deliveryapplication.domain.entity;

import com.pedroferreira.deliveryapplication.domain.enuns.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "sellers")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SellerJpaEntity extends User {

    @OneToOne
    @JoinColumn(name = "store_id", unique = true)
    private Store store;

    @Builder
    public Seller(Long id, String username, String email, String password, String cpf, String phone, String adress, Store store) {
        super(id, username, email, password, cpf, phone, adress);
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
    }
}
