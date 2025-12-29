package com.pedroferreira.deliveryapplication.domain.entity;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "store")
public class Product {

    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Store store;
    private Boolean available = true;
    private Integer preparationTime;
    private Boolean active = true;

    public Product(String name, BigDecimal price, Store store) {
        validateConstructParams(name, price, store);
        this.name = name;
        this.price = price;
        this.store = store;
        this.available = true;
        this.active = true;
    }
    public void activate() {
        this.active = true;
        this.available = true;
    }

    public void deactivate() {
        this.active = false;
        this.available = false;
    }

    public void makeUnavailable() {
        this.available = false;
    }

    public void makeAvailable() {
        if (Boolean.TRUE.equals(this.active)) {
            this.available = true;
        }
    }

    private void validateConstructParams(String name, BigDecimal price, Store store) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        if (store == null) {
            throw new IllegalArgumentException("Loja não pode ser nula");
        }
    }
}
