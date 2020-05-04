package com.restbucks.pact.producer.service;

import com.restbucks.pact.producer.domain.Order;
import com.restbucks.pact.producer.domain.OrderDetails;
import com.restbucks.pact.producer.repository.OrderRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(OrderDetails orderDetails) {
        Order order = new Order(orderDetails, "pending");
        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, OrderDetails orderDetails) {
        Order order = orderRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + id));
        if (order.getStatus().equals("served")) {
            throw new OrderAlreadyServedException(order);
        }
        order.setOrderDetails(orderDetails);
        return orderRepository.save(order);
    }

    public Order findById(Long id) {
        return orderRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + id));
    }
}
