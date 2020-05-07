package com.restbucks.pact.client.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public final class OrderDetails {

    private final String location;
    private final List<Item> items;

    @JsonCreator
    public OrderDetails(@JsonProperty("location") String location, @JsonProperty("items") List<Item> items) {
        this.location = location;
        this.items = items;
    }

    public String getLocation() {
        return location;
    }

    public List<Item> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var orderDetails = (OrderDetails) o;
        return location.equals(orderDetails.location) &&
                items.equals(orderDetails.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, items);
    }
}
