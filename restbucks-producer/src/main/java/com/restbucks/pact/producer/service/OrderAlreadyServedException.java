package com.restbucks.pact.producer.service;

import com.restbucks.pact.producer.domain.Order;

public class OrderAlreadyServedException extends RuntimeException {

    private final Order order;

    public OrderAlreadyServedException(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
