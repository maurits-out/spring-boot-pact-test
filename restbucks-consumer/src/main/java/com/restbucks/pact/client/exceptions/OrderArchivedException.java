package com.restbucks.pact.client.exceptions;

public final class OrderArchivedException extends RestBucksClientException {
    private final long id;

    public OrderArchivedException(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
