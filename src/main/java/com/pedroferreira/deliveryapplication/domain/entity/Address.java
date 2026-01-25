package com.pedroferreira.deliveryapplication.domain.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"customer", "city"})
public class Address {

    @EqualsAndHashCode.Include
    private Long id;

    private Customer customer;
    private City city;
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String zipCode;
    private String reference;
    private Boolean isDefault;
    private Boolean active;

    public Address(Customer customer, City city, String street, String number, String neighborhood, String zipCode) {
        validateContructorParams(customer, city, street, number, neighborhood, zipCode);
        this.customer = customer;
        this.city = city;
        this.street = street;
        this.number = number;
        this.neighborhood = neighborhood;
        this.zipCode = zipCode;
        this.isDefault = false;
        this.active = true;
    }

    public void setAsDefault() {
        this.isDefault = true;
    }

    public void removeAsDefault() {
        this.isDefault = false;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(street).append(", ").append(number);
        if (complement != null && !complement.isBlank()) {
            sb.append(" - ").append(complement);
        }
        sb.append(" - ").append(neighborhood);
        sb.append(", ").append(city.getName()).append("/").append(city.getState());
        sb.append(" - CEP: ").append(zipCode);
        return sb.toString();
    }

    private void validateContructorParams(Customer customer, City city, String street, String number, String neighborhood, String zipCode) {
        if (customer == null) {
            throw new IllegalArgumentException("Cliente é obrigatório");
        }
        if (city == null) {
            throw new IllegalArgumentException("Cidade é obrigatória");
        }
        if (street == null ||  street.isBlank()) {
            throw new IllegalArgumentException("Rua é obrigatória");
        }
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("Número é obrigatório");
        }
        if (neighborhood == null || neighborhood.isBlank()) {
            throw new IllegalArgumentException("Bairro é obrigatório");
        }
        if (zipCode == null || zipCode.isBlank()) {
            throw new IllegalArgumentException("CEP é obrigatório");
        }
    }
}
