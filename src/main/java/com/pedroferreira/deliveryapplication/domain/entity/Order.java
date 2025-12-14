package com.pedroferreira.deliveryapplication.domain.entity;

import com.pedroferreira.deliveryapplication.domain.enuns.StatusOrder;

public class Order {

    private Long id;
    private Customer customer;
    private Store store;
    private List<ItemOrder> itens;
    private StatusOrder status;

    public Order(Customer customer, Store store, List<ItemPedidos> itens){
        this.customer = customer;
        this.store = store;
        this.itens = itens;
        this.status = StatusOrder.CREATED;
    }

    public void confirm(){
        if (status != StatusOrder.CREATED){
            throw new IllegalStateException("Status order already confirmed!");
        }
        this.status = StatusOrder.CONFIRMED;
    }

    public void cancel(){
        if (status == StatusOrder.LEFT_FOR_DELIVERY || status == StatusOrder.DELIVERED){
            throw new IllegalStateException("Status order already cancelled!");
        }

    }
}
