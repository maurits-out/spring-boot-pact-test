package com.restbucks.pact.client.exceptions;

public final class OrderNotFoundException extends RestBucksClientException {
    private final long id;

    public OrderNotFoundException(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
