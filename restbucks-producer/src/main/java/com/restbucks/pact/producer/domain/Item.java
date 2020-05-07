package com.restbucks.pact.producer.domain;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String name;
    private Integer quantity;
    private String milk;
    private String size;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    protected Item() {
    }

    public Item(String name, Integer quantity, String milk, String size) {
        this.name = name;
        this.quantity = quantity;
        this.milk = milk;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getMilk() {
        return milk;
    }

    public String getSize() {
        return size;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
