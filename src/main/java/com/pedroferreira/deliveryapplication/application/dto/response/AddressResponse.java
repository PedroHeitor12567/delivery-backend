package com.pedroferreira.deliveryapplication.application.dto.response;

import com.pedroferreira.deliveryapplication.domain.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
    private Long id;
    private Long customerId;
    private CityResponse city;
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String zipCode;
    private String reference;
    private Boolean isDefault;
    private Boolean active;
    private String fullAddress;

    public static AddressResponse fromEntity(Address address) {
        if (address == null) return null;

        return AddressResponse.builder()
                .id(address.getId())
                .customerId(address.getCustomer().getId())
                .city(CityResponse.fromEntity(address.getCity()))
                .street(address.getStreet())
                .number(address.getNumber())
                .complement(address.getComplement())
                .neighborhood(address.getNeighborhood())
                .zipCode(address.getZipCode())
                .reference(address.getReference())
                .isDefault(address.getIsDefault())
                .active(address.getActive())
                .fullAddress(address.getFullAddress())
                .build();
    }
}
