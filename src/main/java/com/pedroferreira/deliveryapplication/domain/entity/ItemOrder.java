package com.pedroferreira.deliveryapplication.domain.entity;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"order", "product"})
public class ItemOrder {

    @EqualsAndHashCode.Include
    private Long id;
    private Order order;
    private Product product;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal discount = BigDecimal.ZERO;
    private String observations;

    public ItemOrder(Product product, Integer quantity, BigDecimal unitPrice, String observations) {
        validateConstructorParams(product, quantity, unitPrice);
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.observations = observations;
        this.discount = BigDecimal.ZERO;
    }

    public BigDecimal getSubtotal() {
        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        if (discount != null) {
            subtotal = subtotal.subtract(discount);
        }
        return subtotal;
    }

    public void validate() {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço unitário deve ser maior que zero");
        }
        if (product == null) {
            throw new IllegalStateException("Produto não pode ser nulo");
        }
        if (!Boolean.TRUE.equals(product.getAvailable())) {
            throw new IllegalStateException("Produto não está disponível: " + product.getName());
        }
    }

    private void validateConstructorParams(Product product, Integer quantity, BigDecimal unitPrice) {
        if (product == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço unitário deve ser maior do que zero");
        }
    }
}
