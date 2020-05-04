package com.restbucks.pact.producer.web;

import com.restbucks.pact.producer.domain.Order;
import com.restbucks.pact.producer.domain.OrderDetails;
import com.restbucks.pact.producer.service.OrderAlreadyServedException;
import com.restbucks.pact.producer.service.OrderNotFoundException;
import com.restbucks.pact.producer.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.*;

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
    ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody OrderDetails orderDetails) {
        try {
            return ok(orderService.updateOrder(id, orderDetails));
        } catch (OrderAlreadyServedException ex) {
            return status(CONFLICT).body(ex.getOrder());
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<Order> getOrder(@PathVariable Long id) {
        try {
            return ok(orderService.findById(id));
        } catch (OrderNotFoundException ex) {
            return status(NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Object> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return noContent().build();
        } catch (OrderNotFoundException ex) {
            return status(NOT_FOUND).build();
        } catch (OrderAlreadyServedException ex) {
            return status(METHOD_NOT_ALLOWED).build();
        }
    }
}