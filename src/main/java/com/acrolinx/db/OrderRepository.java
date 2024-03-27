package com.acrolinx.db;

import com.acrolinx.db.entity.OrderEntity;

import java.util.Optional;

public interface OrderRepository {

  Optional<OrderEntity> getOrder(String orderId);

  Optional<String> createOrder(OrderEntity order);

  Optional<OrderEntity> replace(OrderEntity order);

  Optional<OrderEntity> update(OrderEntity order);

  Optional<OrderEntity> delete(String orderId);
}
