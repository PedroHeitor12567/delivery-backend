package com.pedroferreira.deliveryapplication.domain.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class City {

    @EqualsAndHashCode.Include
    private Long id;

    private String name;
    private String state;
    private Boolean active;

    public City(String name, String state) {
        validateContructorParams(name, state);
        this.name = name;
        this.state = state;
        this.active = true;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public boolean isActive() {
        return Boolean.TRUE.equals(this.active);
    }

    private void validateContructorParams(String name, String state) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome da cidade é obrigatório");
        }
        if (state == null || state.isBlank()) {
            throw new IllegalArgumentException("Estado é obrigatório");
        }
    }
}
