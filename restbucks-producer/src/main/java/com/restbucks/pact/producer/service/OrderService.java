package com.restbucks.pact.producer.service;

import com.restbucks.pact.producer.domain.Order;
import com.restbucks.pact.producer.domain.OrderDetails;
import com.restbucks.pact.producer.repository.OrderRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(OrderDetails orderDetails) {
        Order order = new Order(orderDetails, "pending");
        orderRepository.save(order);
        return order;
    }
}
