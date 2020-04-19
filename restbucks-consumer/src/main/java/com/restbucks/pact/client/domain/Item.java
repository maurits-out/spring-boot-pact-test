package com.restbucks.pact.client.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Item {

    private final String name;
    private final int quantity;
    private final String milk;
    private final String size;

    @JsonCreator
    public Item(@JsonProperty("name") String name,
                @JsonProperty("quantity") int quantity,
                @JsonProperty("milk") String milk,
                @JsonProperty("size") String size) {
        this.name = name;
        this.quantity = quantity;
        this.milk = milk;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMilk() {
        return milk;
    }

    public String getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return quantity == item.quantity &&
                name.equals(item.name) &&
                milk.equals(item.milk) &&
                size.equals(item.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, quantity, milk, size);
    }
}
