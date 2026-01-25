package com.pedroferreira.deliveryapplication.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cities", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "state"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 2)
    private String state;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<StoreJpaEntity> stores = new ArrayList<>();

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<AddressJpaEntity> addresses = new ArrayList<>();
}
