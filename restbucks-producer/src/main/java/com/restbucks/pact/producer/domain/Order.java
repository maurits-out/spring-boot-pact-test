package com.restbucks.pact.producer.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Embedded
    private OrderDetails orderDetails;

    private String status;

    protected Order() {
    }

    public Order(OrderDetails orderDetails, String status) {
        this.orderDetails = orderDetails;
        this.orderDetails.updateOrder(this);
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    @JsonProperty("details")
    public OrderDetails getOrderDetails() {
        return orderDetails;
    }

    public String getStatus() {
        return status;
    }
}
