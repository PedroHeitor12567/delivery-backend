package com.pedroferreira.deliveryapplication.domain.enuns;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static com.pedroferreira.deliveryapplication.domain.enuns.EventRequest.*;

public enum StatusOrder {

    CREATED(Set.of(UserRole.CUSTOMER)),
    CONFIRMED(Set.of(UserRole.SELLER)),
    READY(Set.of(UserRole.SELLER)),
    LEFT_FOR_DELIVERY(Set.of(UserRole.SYSTEM)),
    DELIVERED(Set.of()),
    CANCELED(Set.of());

    private final Set<UserRole> rolesAllowed;

    StatusOrder(Set<UserRole> rolesAllowed) {
        this.rolesAllowed = rolesAllowed;
    }

    private Map<EventRequest, StatusOrder> getTransitions() {
        return switch (this) {
            case CREATED -> Map.of(
                    CONFIRM, CONFIRMED,
                    CANCEL, CANCELED
            );
            case CONFIRMED -> Map.of(
                    MARK_POINT, READY,
                    REFUSE, CANCELED
            );
            case READY -> Map.of(
                    EXIT_FOR_DELIVERY, LEFT_FOR_DELIVERY
            );
            case LEFT_FOR_DELIVERY -> Map.of(
                    DELIVER, DELIVERED
            );
            case DELIVERED, CANCELED -> Collections.emptyMap();
        };
    }

    public StatusOrder apply(EventRequest request, UserRole role) {
        Map<EventRequest, StatusOrder> transitions = getTransitions();

        if (!transitions.containsKey(request)) {
            throw new IllegalStateException(
                    "Evento " + request + " não permitido no status " + this
            );
        }

        if (!rolesAllowed.contains(role)) {
            throw new IllegalStateException(
                    "Papel " + role + " não pode executar " + request + " no status " + this
            );
        }
        return transitions.get(request);
    }
    public boolean ehFinal() {
        return getTransitions().isEmpty();
    }
}