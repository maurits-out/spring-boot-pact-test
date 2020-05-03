package com.restbucks.pact.producer.web;

import com.restbucks.pact.producer.domain.Order;
import com.restbucks.pact.producer.domain.OrderDetails;
import com.restbucks.pact.producer.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    Order createOrder(@RequestBody OrderDetails orderDetails) {
        return orderService.createOrder(orderDetails);
    }

}