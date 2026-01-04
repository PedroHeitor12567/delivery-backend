package com.pedroferreira.deliveryapplication.application.dto.response;

import com.pedroferreira.deliveryapplication.domain.entity.Seller;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerResponse {
    private Long id;
    private String username;
    private String email;
    private String cpf;
    private String phone;
    private String address;
    private Boolean active;
    private StoreSimpleResponse store;

    public static SellerResponse fromEntity(Seller seller) {
        return SellerResponse.builder()
                .id(seller.getId())
                .username(seller.getUsername())
                .email(seller.getEmail())
                .cpf(seller.getCpf())
                .phone(seller.getPhone())
                .address(seller.getAddress())
                .active(seller.getActive())
                .store(seller.getStore() != null ?
                        StoreSimpleResponse.fromEntity(seller.getStore()) : null)
                .build();
    }
}
