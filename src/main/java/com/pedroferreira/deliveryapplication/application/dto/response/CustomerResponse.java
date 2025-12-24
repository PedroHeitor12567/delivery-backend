package com.pedroferreira.deliveryapplication.application.dto.response;

import com.pedroferreira.deliveryapplication.domain.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

    private Long id;
    private String username;
    private String email;
    private String cpf;
    private String phone;
    private String address;
    private Integer loyaltyPoints;
    private Boolean active;

    public static CustomerResponse fromEntity(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .username(customer.getUsername())
                .email(customer.getEmail())
                .cpf(customer.getCpf())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .loyaltyPoints(customer.getLoyaltyPoints())
                .active(customer.getActive())
                .build();
    }
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class CustomerSimpleResponse {
    private Long id;
    private String username;

    public static CustomerSimpleResponse fromEntity(Customer customer) {
        return CustomerSimpleResponse.builder()
                .id(customer.getId())
                .username(customer.getUsername())
                .build();
    }
}
