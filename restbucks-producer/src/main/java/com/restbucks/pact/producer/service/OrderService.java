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
        var order = new Order(orderDetails, "pending");
        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, OrderDetails orderDetails) {
        var order = orderRepository
                .findById(id)
                .orElseThrow(OrderNotFoundException::new);
        if (order.getStatus().equals("served")) {
            throw new OrderAlreadyServedException(order);
        }
        order.setOrderDetails(orderDetails);
        return orderRepository.save(order);
    }

    public void cancelOrder(Long id) {
        var order = orderRepository
                .findById(id)
                .orElseThrow(OrderNotFoundException::new);
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException();
        }
        if (order.getStatus().equals("served")) {
            throw new OrderAlreadyServedException(order);
        }
        orderRepository.deleteById(id);
    }

    public Order findById(Long id) {
        return orderRepository
                .findById(id)
                .orElseThrow(OrderNotFoundException::new);
    }
}
