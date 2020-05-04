package com.restbucks.pact.producer;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import com.restbucks.pact.producer.domain.Item;
import com.restbucks.pact.producer.domain.Order;
import com.restbucks.pact.producer.domain.OrderDetails;
import com.restbucks.pact.producer.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Provider("RestBucksProvider")
@PactBroker
public class RestBucksProviderContractIT {

    @LocalServerPort
    private int serverPort;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setupTestTarget(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", serverPort, "/"));
        orderRepository.deleteAll();
    }

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State("a pending order with Caffe Latte and milk set to 'whole'")
    Map<String, Long> toPendingOrderState() {
        Item item = new Item("latte", 1, "whole", "small");
        OrderDetails orderDetails = new OrderDetails("takeAway", List.of(item));

        Order order = orderRepository.save(new Order(orderDetails, "pending"));

        return Map.of("id", order.getId());
    }

    @State("a served order")
    Map<String, Long> toServedOrderState() {
        Item item = new Item("cappuccino", 2, "skim", "large");
        OrderDetails orderDetails = new OrderDetails("takeAway", List.of(item));

        Order order = orderRepository.save(new Order(orderDetails, "served"));

        return Map.of("id", order.getId());
    }

    @State("an order")
    Map<String, Long> toOrderState() {
        return toServedOrderState();
    }

    @State("no order exists with id 1")
    void toMissingOrderState() {
        if (orderRepository.existsById(1L)) {
            orderRepository.deleteById(1L);
        }
    }
}
