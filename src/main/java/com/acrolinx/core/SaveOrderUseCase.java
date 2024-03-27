package com.acrolinx.core;

import com.acrolinx.core.domain.Order;

import java.util.Optional;

public interface SaveOrderUseCase {

  Optional<String> createOrder(Order order);

  Optional<Order> updateOrder(String orderId, Order order);

  Optional<Order> partialUpdateOrder(String orderId, Order order);

  Optional<Order> deleteOrder(String orderId);

  Optional<Order> getOrder(String orderId);
}
