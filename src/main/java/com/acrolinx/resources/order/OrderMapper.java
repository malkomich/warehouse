package com.acrolinx.resources.order;

import com.acrolinx.api.OrderItem;
import com.acrolinx.api.Status;
import com.acrolinx.api.request.NewOrderRequest;
import com.acrolinx.api.request.OrderPartialUpdateRequest;
import com.acrolinx.api.request.OrderUpdateRequest;
import com.acrolinx.api.response.OrderStatus;
import com.acrolinx.core.domain.Order;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

  private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private OrderMapper() {}

  static OrderStatus toOrderStatus(Order order) {
    return new OrderStatus(
        order.getId(),
        mapOrderItemsToAPI(order.getOrderItems()),
        order.getShipDate(),
        mapStatus(order.getStatus()));
  }

  static Order toOrder(NewOrderRequest newOrderRequest) {
    return Order.builder()
        .orderItems(mapOrderItemsToDomain(newOrderRequest.getOrderItems()))
        .build();
  }

  static Order toOrder(OrderUpdateRequest orderUpdateRequest) {
    return Order.builder()
        .shipDate(LocalDate.parse(orderUpdateRequest.getShipDate(), DATETIME_FORMATTER))
        .status(mapStatus(orderUpdateRequest.getStatus()))
        .orderItems(mapOrderItems(orderUpdateRequest.getOrderItems()))
        .build();
  }

  private static List<com.acrolinx.core.domain.OrderItem> mapOrderItems(List<OrderItem> orderItems) {
    return orderItems.stream()
        .map(orderItem -> com.acrolinx.core.domain.OrderItem.builder()
            .productId(orderItem.getProductId())
            .quantity(orderItem.getQuantity())
            .build())
        .collect(Collectors.toList());
  }

  static Order toOrder(OrderPartialUpdateRequest orderPartialUpdateRequest) {
    return Order.builder()
        .shipDate(LocalDate.parse(orderPartialUpdateRequest.getShipDate(), DATETIME_FORMATTER))
        .status(mapStatus(orderPartialUpdateRequest.getStatus()))
        .build();
  }

  private static Status mapStatus(Order.Status status) {

    if (Order.Status.APPROVED.equals(status)) {
      return Status.APPROVED;
    }

    if (Order.Status.DELIVERED.equals(status)) {
      return Status.DELIVERED;
    }

    return Status.PLACED;
  }

  private static Order.Status mapStatus(Status status) {

    if (Status.APPROVED.equals(status)) {
      return Order.Status.APPROVED;
    }

    if (Status.DELIVERED.equals(status)) {
      return Order.Status.DELIVERED;
    }

    return Order.Status.PLACED;
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
