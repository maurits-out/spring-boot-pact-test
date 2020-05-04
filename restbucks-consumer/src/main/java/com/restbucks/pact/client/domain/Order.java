package com.restbucks.pact.client.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public final class Order {

    private final long id;
    private final OrderDetails details;
    private final String status;

    @JsonCreator
    public Order(@JsonProperty("id") long id,
                 @JsonProperty("details") OrderDetails details,
                 @JsonProperty("status") String status) {
        this.id = id;
        this.details = details;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public OrderDetails getDetails() {
        return details;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id &&
                details.equals(order.details) &&
                status.equals(order.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, details, status);
    }
}
