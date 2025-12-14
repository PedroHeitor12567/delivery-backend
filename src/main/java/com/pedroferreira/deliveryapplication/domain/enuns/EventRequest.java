package com.pedroferreira.deliveryapplication.domain.enuns;

public enum EventRequest {

    CREATE,
    CONFIRM,
    REFUSE,
    MARK_POINT,
    EXIT_FOR_DELIVERY,
    DELIVER,
    CANCEL;

    public boolean ehCancellation(){
        return this == CANCEL;
    }

    public boolean ehDeliver(){
        return this == DELIVER;
    }
}
