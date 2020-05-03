package com.restbucks.pact.client;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.restbucks.pact.client.domain.Item;
import com.restbucks.pact.client.domain.Order;
import com.restbucks.pact.client.domain.OrderDetails;
import com.restbucks.pact.client.exceptions.OrderAlreadyServedException;
import com.restbucks.pact.client.exceptions.OrderArchivedException;
import com.restbucks.pact.client.exceptions.OrderNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonBody;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "RestBucksProvider")
public class RestBucksClientTest {

    private RestBucksClient client;

    @BeforeEach
    public void setUp(MockServer mockServer) {
        assertNotNull(mockServer);
        client = new RestBucksClient(mockServer.getUrl());
    }

    @Pact(consumer = "RestBucksClient")
    public RequestResponsePact placeOrder(PactDslWithProvider builder) {
        return builder
                .uponReceiving("a request to create a coffee order")
                .path("/order")
                .method("POST")
                .body(newJsonBody(details -> {
                    details.stringValue("location", "takeAway");
                    details.array("items", items -> {
                        items.object(item -> {
                            item.stringValue("name", "latte");
                            item.numberValue("quantity", 1);
                            item.stringValue("milk", "whole");
                            item.stringValue("size", "small");
                        });
                    });
                }).build())
                .willRespondWith()
                .status(200)
                .body(newJsonBody(order -> {
                    order.id("id");
                    order.stringValue("status", "pending");
                    order.object("details", details -> {
                        details.stringValue("location", "takeAway");
                        details.array("items", items -> {
                            items.object(item -> {
                                item.stringValue("name", "latte");
                                item.numberValue("quantity", 1);
                                item.stringValue("milk", "whole");
                                item.stringValue("size", "small");
                            });
                        });
                    });
                }).build())
                .toPact();
    }

    @Pact(consumer = "RestBucksClient")
    public RequestResponsePact order(PactDslWithProvider builder) {
        return builder
                .given("a pending order")
                .uponReceiving("a request to retrieve the order")
                .path("/order/1")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(newJsonBody(order -> {
                    order.id("id", 1L);
                    order.stringValue("status", "pending");
                    order.object("details", details -> {
                        details.stringValue("location", "takeAway");
                        details.array("items", items -> {
                            items.object(item -> {
                                item.stringValue("name", "latte");
                                item.numberValue("quantity", 1);
                                item.stringValue("milk", "whole");
                                item.stringValue("size", "small");
                            });
                        });
                    });
                }).build())
                .toPact();
    }

    @Pact(consumer = "RestBucksClient")
    public RequestResponsePact updatePendingOrder(PactDslWithProvider builder) {
        return builder
                .given("a pending order")
                .uponReceiving("a request to update the pending order")
                .pathFromProviderState("/order/${id}", "/order/1")
                .method("PUT")
                .body(newJsonBody(details -> {
                    details.stringValue("location", "takeAway");
                    details.array("items", items -> {
                        items.object(item -> {
                            item.stringValue("name", "latte");
                            item.numberValue("quantity", 1);
                            item.stringValue("milk", "skim");
                            item.stringValue("size", "small");
                        });
                    });
                }).build())
                .willRespondWith()
                .status(200)
                .body(newJsonBody(order -> {
                    order.id("id", 1L);
                    order.stringValue("status", "pending");
                    order.object("details", details -> {
                        details.stringValue("location", "takeAway");
                        details.array("items", items -> {
                            items.object(item -> {
                                item.stringValue("name", "latte");
                                item.numberValue("quantity", 1);
                                item.stringValue("milk", "skim");
                                item.stringValue("size", "small");
                            });
                        });
                    });
                }).build())
                .toPact();
    }

    @Pact(consumer = "RestBucksClient")
    public RequestResponsePact updateServedOrder(PactDslWithProvider builder) {
        return builder
                .given("a served order")
                .uponReceiving("a request to update the order")
                .path("/order/1")
                .method("PUT")
                .body(newJsonBody(details -> {
                    details.stringValue("location", "takeAway");
                    details.array("items", items -> {
                        items.object(item -> {
                            item.stringValue("name", "latte");
                            item.numberValue("quantity", 1);
                            item.stringValue("milk", "skim");
                            item.stringValue("size", "small");
                        });
                    });
                }).build())
                .willRespondWith()
                .status(409)
                .body(newJsonBody(order -> {
                    order.id("id", 1L);
                    order.stringValue("status", "served");
                    order.object("details", details -> {
                        details.stringValue("location", "takeAway");
                        details.array("items", items -> {
                            items.object(item -> {
                                item.stringValue("name", "latte");
                                item.numberValue("quantity", 1);
                                item.stringValue("milk", "skim");
                                item.stringValue("size", "small");
                            });
                        });
                    });
                }).build())
                .toPact();
    }

    @Pact(consumer = "RestBucksClient")
    public RequestResponsePact orderDoesNotExist(PactDslWithProvider builder) {
        return builder
                .uponReceiving("a request to retrieve a non existing order")
                .path("/order/1")
                .method("GET")
                .willRespondWith()
                .status(404)
                .toPact();
    }

    @Pact(consumer = "RestBucksClient")
    public RequestResponsePact deletePendingOrder(PactDslWithProvider builder) {
        return builder
                .given("a pending order")
                .uponReceiving("a request to a delete the order")
                .path("/order/1")
                .method("DELETE")
                .willRespondWith()
                .status(204)
                .toPact();
    }

    @Pact(consumer = "RestBucksClient")
    public RequestResponsePact deleteOrderDoesNotExist(PactDslWithProvider builder) {
        return builder
                .uponReceiving("a request to delete the order")
                .path("/order/1")
                .method("DELETE")
                .willRespondWith()
                .status(404)
                .toPact();
    }

    @Pact(consumer = "RestBucksClient")
    public RequestResponsePact deleteServedOrder(PactDslWithProvider builder) {
        return builder
                .given("a served order")
                .uponReceiving("a request to delete the order")
                .path("/order/1")
                .method("DELETE")
                .willRespondWith()
                .status(405)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "placeOrder")
    public void testPlaceOrder() {
        Item item = new Item("latte", 1, "whole", "small");
        OrderDetails orderDetails = new OrderDetails("takeAway", List.of(item));

        Order order = client.bookOrder(orderDetails);

        assertAll(
                () -> assertNotNull(order),
                () -> assertTrue(order.getId() > 0),
                () -> assertEquals("pending", order.getStatus()),
                () -> assertEquals(orderDetails, order.getDetails()));
    }

    @Test
    @PactTestFor(pactMethod = "order")
    public void testGetOrder() {
        Order order = client.getOrder(1);

        assertAll(
                () -> assertNotNull(order),
                () -> assertEquals(1, order.getId()),
                () -> assertEquals("pending", order.getStatus()),
                () -> assertEquals("takeAway", order.getDetails().getLocation()),
                () -> assertFalse(order.getDetails().getItems().isEmpty()),
                () -> assertEquals(new Item("latte", 1, "whole", "small"), order.getDetails().getItems().get(0)));
    }

    @Test
    @PactTestFor(pactMethod = "orderDoesNotExist")
    public void testOrderDoesNotExist() {
        OrderNotFoundException ex = assertThrows(OrderNotFoundException.class, () -> client.getOrder(1L));
        assertEquals(1, ex.getId());
    }

    @Test
    @PactTestFor(pactMethod = "updatePendingOrder")
    public void testUpdatePendingOrder() {
        Item item = new Item("latte", 1, "skim", "small");
        OrderDetails orderDetails = new OrderDetails("takeAway", List.of(item));

        Order order = client.updateOrder(1, orderDetails);

        assertAll(
                () -> assertNotNull(order),
                () -> assertEquals(1, order.getId()),
                () -> assertEquals("pending", order.getStatus()),
                () -> assertEquals(orderDetails, order.getDetails()));
    }

    @Test
    @PactTestFor(pactMethod = "updateServedOrder")
    public void testUpdateServedOrder() {
        Item item = new Item("latte", 1, "skim", "small");
        OrderDetails orderDetails = new OrderDetails("takeAway", List.of(item));

        OrderAlreadyServedException ex = assertThrows(OrderAlreadyServedException.class,
                () -> client.updateOrder(1, orderDetails));

        Order order = ex.getOrder();
        assertAll(
                () -> assertNotNull(order),
                () -> assertEquals(1, order.getId()),
                () -> assertEquals("served", order.getStatus()),
                () -> assertEquals(orderDetails, order.getDetails()));
    }

    @Test
    @PactTestFor(pactMethod = "deletePendingOrder")
    public void testDeletePendingOrder() {
        client.deleteOrder(1);
    }

    @Test
    @PactTestFor(pactMethod = "deleteOrderDoesNotExist")
    public void testDeleteOrderDoesNotExist() {
        OrderNotFoundException ex = assertThrows(OrderNotFoundException.class, () -> client.deleteOrder(1L));
        assertEquals(1, ex.getId());
    }

    @Test
    @PactTestFor(pactMethod = "deleteServedOrder")
    public void testDeleteServedOrder() {
        OrderArchivedException ex = assertThrows(OrderArchivedException.class, () -> client.deleteOrder(1L));
        assertEquals(1, ex.getId());
    }
}
