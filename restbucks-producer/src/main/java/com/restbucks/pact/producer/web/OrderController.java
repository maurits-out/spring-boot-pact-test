package com.restbucks.pact.producer.web;

import com.restbucks.pact.producer.domain.Order;
import com.restbucks.pact.producer.domain.OrderDetails;
import com.restbucks.pact.producer.service.OrderService;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}")
    Order updateOrder(@PathVariable Long id, @RequestBody OrderDetails orderDetails) {
        return orderService.updateOrder(id, orderDetails);
    }

}