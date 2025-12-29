package com.pedroferreira.deliveryapplication.domain.entity;

import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"products", "createdBy"})
public class Store {

    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String description;
    private String city;
    private String state;

    @Builder.Default
    private List<Product> products = new ArrayList<>();

    @Builder.Default
    private Integer totalSales = 0;

    private String phone;
    private String email;
    private String address;
    private String category;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private BigDecimal deliveryFeePerKm;
    private BigDecimal baseDeliveryFee;
    private BigDecimal minimumOrder;

    private Admin createdBy;

    @Builder.Default
    private Boolean active = true;

    @Builder.Default
    private Boolean open = false;

    @Builder.Default
    private BigDecimal rating = BigDecimal.ZERO;

    @Builder.Default
    private Integer totalRatings = 0;

    public Store(String name, String city, String state, String phone, String email, String address, String category, BigDecimal deliveryFeePerKm, BigDecimal baseDeliveryFee, BigDecimal minimumOrder) {
        this.name = name;
        this.city = city;
        this.state = state;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.category = category;
        this.deliveryFeePerKm = deliveryFeePerKm;
        this.baseDeliveryFee = baseDeliveryFee;
        this.minimumOrder = minimumOrder;
        this.active = true;
        this.open = false;
        this.totalSales = 0;
        this.rating = BigDecimal.ZERO;
        this.totalRatings = 0;
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo");
        }
        products.add(product);
        product.setStore(this);
    }

    public void removeProduct(Product product) {
        products.remove(product);
        if (product != null) {
            product.setStore(null);
        }
    }

    public void incrementSales() {
        this.totalSales++;
    }

    public void addRating(Integer stars) {
        if (stars < 1 || stars > 5) {
            throw new IllegalArgumentException("Avaliação deve ser entre 1 e 5");
        }
        BigDecimal totalPoints = rating.multiply(BigDecimal.valueOf(totalRatings));
        totalRatings++;
        this.rating = totalPoints.add(BigDecimal.valueOf(stars)).divide(BigDecimal.valueOf(totalRatings), 2, RoundingMode.HALF_UP);
    }

    public boolean isOpenNow() {
        if (!Boolean.TRUE.equals(active) || !Boolean.TRUE.equals(open)) {
            return false;
        }

        if (openingTime == null || closingTime == null) {
            return false;
        }
        LocalTime now = LocalTime.now();
        return now.isAfter(openingTime) && now.isBefore(closingTime);
    }

    public void openStore() {
        if (!Boolean.TRUE.equals(active)) {
            throw new IllegalStateException("Loja desativada não pode ser aberta");
        }
        this.open = true;
    }

    public void closeStore() {
        if (!Boolean.TRUE.equals(active)) {
            throw new IllegalStateException("Loja desativada não pode ser aberta");
        }
        this.open = true;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
        this.open = false;
    }

    public BigDecimal calculateDeliveryFee(Double distanceKm) {
        if (distanceKm == null || distanceKm <= 0) {
            throw new IllegalArgumentException("Distância inválida");
        }

        BigDecimal distanceFee = deliveryFeePerKm
                .multiply(BigDecimal.valueOf(distanceKm));

        BigDecimal totalFee = baseDeliveryFee.add(distanceFee);

        return totalFee.setScale(2, RoundingMode.HALF_UP);
    }

    private void validateConstructorParams(String name, String city, String state, String phone, String email, String address, String category, BigDecimal deliveryFeePerKm, BigDecimal baseDeliveryFee, BigDecimal minimumOrder) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome da loja é obrigatório");
        }
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("Cidade é obrigatória");
        }
        if (state == null || state.isBlank()) {
            throw new IllegalArgumentException("Estado é obrigatório");
        }
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Telefone é obrigatório");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Endereço é obrigatório");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Categoria é obrigatória");
        }
        if (deliveryFeePerKm == null || deliveryFeePerKm.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Taxa por KM deve ser maior ou igual a zero");
        }
        if (baseDeliveryFee == null || baseDeliveryFee.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Taxa base deve ser maior ou igual a zero");
        }
        if (minimumOrder == null || minimumOrder.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Pedido mínimo deve ser maior do que zero");
        }
    }

}
