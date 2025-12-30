package com.pedroferreira.deliveryapplication.infrastructure.persistence.entity;

import com.pedroferreira.deliveryapplication.domain.entity.Order;
import com.pedroferreira.deliveryapplication.domain.entity.Seller;
import com.pedroferreira.deliveryapplication.domain.entity.Store;
import com.pedroferreira.deliveryapplication.domain.entity.User;
import com.pedroferreira.deliveryapplication.domain.enuns.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sellers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Boolean active;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @OneToOne
    @JoinColumn(name = "store_id")
    private StoreJpaEntity store;

    public static SellerJpaEntity fromDomain(Seller seller) {
        if (seller == null)  return null;

        return SellerJpaEntity.builder()
                .id(seller.getId())
                .username(seller.getUsername())
                .email(seller.getEmail())
                .password(seller.getPassword())
                .cpf(seller.getCpf())
                .phone(seller.getPhone())
                .address(seller.getAddress())
                .active(seller.getActive())
                .role(seller.getRole())
                .build();
    }

    public Seller toDomain() {
        Seller seller = Seller.builder()
                .id(this.id)
                .username(this.username)
                .email(this.email)
                .password(this.password)
                .cpf(this.cpf)
                .phone(this.phone)
                .address(this.address)
                .build();

        seller.setActive(this.active);
        seller.setRole(this.role);

        if (this.store != null) {
            seller.setStore(this.store.toDomain());
        }

        return seller;
    }
}
