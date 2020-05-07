package com.restbucks.pact.client;

import com.restbucks.pact.client.domain.Order;
import com.restbucks.pact.client.domain.OrderDetails;
import com.restbucks.pact.client.exceptions.OrderAlreadyServedException;
import com.restbucks.pact.client.exceptions.OrderArchivedException;
import com.restbucks.pact.client.exceptions.OrderNotFoundException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import static java.net.HttpURLConnection.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

/**
 * The RestBucksClient is a simple Java class that uses JAX-WS to call the RestBucks API.
 */
public final class RestBucksClient {

    private static final MediaType JSON_UTF_8_MEDIA_TYPE = APPLICATION_JSON_TYPE.withCharset(UTF_8.displayName());

    private final String orderUrl;

    private final Client client = ClientBuilder.newClient();

    /**
     * Constructor.
     *
     * @param baseUrl the base URL of the endpoint that exposes the RestBucks API (e.g. http://localhost:8080)
     */
    public RestBucksClient(String baseUrl) {
        this.orderUrl = baseUrl + "/order";
    }

    public Order bookOrder(OrderDetails orderDetails) {
        return client
                .target(orderUrl)
                .request(APPLICATION_JSON)
                .post(entity(orderDetails, JSON_UTF_8_MEDIA_TYPE))
                .readEntity(Order.class);
    }

    public Order getOrder(long id) {
        var response = client.target(orderUrl + "/" + id)
                .request(APPLICATION_JSON)
                .get();
        if (response.getStatus() == HTTP_NOT_FOUND) {
            throw new OrderNotFoundException(id);
        }
        return response.readEntity(Order.class);
    }

    public Order updateOrder(long id, OrderDetails orderDetails) {
        var response = client.target(orderUrl + "/" + id)
                .request(APPLICATION_JSON)
                .put(entity(orderDetails, JSON_UTF_8_MEDIA_TYPE));
        if (response.getStatus() == HTTP_NOT_FOUND) {
            throw new OrderNotFoundException(id);
        }
        var order = response.readEntity(Order.class);
        if (response.getStatus() == HTTP_CONFLICT) {
            throw new OrderAlreadyServedException(order);
        }
        return order;
    }

    public void cancelOrder(long id) {
        var response = client.target(orderUrl + "/" + id)
                .request(APPLICATION_JSON)
                .delete();
        if (response.getStatus() == HTTP_NOT_FOUND) {
            throw new OrderNotFoundException(id);
        }
        if (response.getStatus() == HTTP_BAD_METHOD) {
            throw new OrderArchivedException(id);
        }
    }

    public void close() {
        client.close();
    }
}
