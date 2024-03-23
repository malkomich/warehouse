package com.acrolinx.core;

import com.acrolinx.core.domain.Order;

import java.util.Optional;

public interface SaveOrderUseCase {

  Order createOrder(Order order);

  Optional<Order> updateOrder(Integer orderId, Order order);

  Optional<Order> deleteOrder(Integer orderId);

  Optional<Order> getOrder(Integer orderId);
}
