package com.restbucks.pact.producer.repository;

import com.restbucks.pact.producer.domain.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
