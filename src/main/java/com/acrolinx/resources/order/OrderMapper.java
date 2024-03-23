package com.acrolinx.resources.order;

import com.acrolinx.api.OrderItem;
import com.acrolinx.api.request.OrderRequest;
import com.acrolinx.api.response.OrderStatus;
import com.acrolinx.core.domain.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

  static OrderStatus toOrderStatus(Order order) {
    return new OrderStatus(
        order.getId(),
        mapOrderItemsToAPI(order.getOrderItems()),
        order.getShipDate(),
        mapStatus(order.getStatus()));
  }

  static Order toOrder(OrderRequest orderRequest) {
    return Order.builder()
        .orderItems(mapOrderItemsToDomain(orderRequest.getOrderItems()))
        .build();
  }

  private static OrderStatus.Status mapStatus(Order.Status status) {

    if (Order.Status.APPROVED.equals(status)) {
      return OrderStatus.Status.APPROVED;
    }

    if (Order.Status.DELIVERED.equals(status)) {
      return OrderStatus.Status.DELIVERED;
    }

    return OrderStatus.Status.PLACED;
  }

  private static List<OrderItem> mapOrderItemsToAPI(List<com.acrolinx.core.domain.OrderItem> orderItems) {
    return orderItems.stream()
        .map(orderItem -> new OrderItem(orderItem.getProductId(), orderItem.getQuantity()))
        .collect(Collectors.toList());
  }

  private static List<com.acrolinx.core.domain.OrderItem> mapOrderItemsToDomain(List<OrderItem> orderItems) {
    return orderItems.stream()
        .map(orderItem -> com.acrolinx.core.domain.OrderItem.builder()
            .productId(orderItem.getProductId())
            .quantity(orderItem.getQuantity())
            .build())
        .collect(Collectors.toList());
  }
}
