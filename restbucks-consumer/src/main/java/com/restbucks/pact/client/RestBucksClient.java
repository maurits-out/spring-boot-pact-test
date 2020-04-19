package com.restbucks.pact.client;

import com.restbucks.pact.client.domain.Order;
import com.restbucks.pact.client.domain.OrderDetails;
import com.restbucks.pact.client.exceptions.OrderAlreadyServedException;
import com.restbucks.pact.client.exceptions.OrderArchivedException;
import com.restbucks.pact.client.exceptions.OrderNotFoundException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

public class RestBucksClient {

    private final String baseUrl;

    private final Client client = ClientBuilder.newClient();

    public RestBucksClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Order bookOrder(OrderDetails orderDetails) {
        return client
                .target(baseUrl + "/order")
                .request(APPLICATION_JSON)
                .post(entity(orderDetails, APPLICATION_JSON_TYPE.withCharset("UTF-8")))
                .readEntity(Order.class);
    }

    public Order getOrder(long id) {
        Response response = client.target(baseUrl + "/order/" + id)
                .request(APPLICATION_JSON)
                .get();
        if (response.getStatus() == 404) {
            throw new OrderNotFoundException(id);
        }
        return response.readEntity(Order.class);
    }

    public Order updateOrder(long id, OrderDetails orderDetails) {
        Response response = client.target(baseUrl + "/order/" + id)
                .request(APPLICATION_JSON)
                .put(entity(orderDetails, APPLICATION_JSON_TYPE.withCharset("UTF-8")));
        Order order = response.readEntity(Order.class);
        if (response.getStatus() == 409) {
            throw new OrderAlreadyServedException(order);
        }
        return order;
    }

    public void deleteOrder(long id) {
        Response response = client.target(baseUrl + "/order/" + id)
                .request(APPLICATION_JSON)
                .delete();
        if (response.getStatus() == 404) {
            throw new OrderNotFoundException(id);
        }
        if (response.getStatus() == 405) {
            throw new OrderArchivedException(id);
        }
    }
}
