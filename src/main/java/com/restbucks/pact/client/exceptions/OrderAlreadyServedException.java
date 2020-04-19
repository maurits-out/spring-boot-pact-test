package com.restbucks.pact.client.exceptions;

import com.restbucks.pact.client.domain.Order;

public class OrderAlreadyServedException extends RestBucksClientException {

    private final Order order;

    public OrderAlreadyServedException(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
