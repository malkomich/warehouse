package com.acrolinx.core;

import com.acrolinx.core.domain.Order;
import com.acrolinx.core.domain.OrderItem;
import com.acrolinx.db.OrderRepository;
import com.acrolinx.db.entity.OrderEntity;
import com.acrolinx.db.entity.OrderItemEntity;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OrderService implements SaveOrderUseCase {

  private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

  private final OrderRepository orderRepository;

  @Override
  public Optional<String> createOrder(Order order) {

    var orderEntity = new OrderEntity();
    orderEntity.setOrderItems(toEntity(order.getOrderItems()));
    orderEntity.setStatus(Order.Status.PLACED.name());

    return orderRepository.createOrder(orderEntity);
  }

  @Override
  public Optional<Order> updateOrder(String orderId, Order order) {

    var orderEntity = toEntity(orderId, order);

    return Optional.ofNullable(orderEntity)
        .flatMap(orderRepository::replace)
        .map(this::toDomain);
  }

  @Override
  public Optional<Order> partialUpdateOrder(String orderId, Order order) {

    var orderEntity = toEntity(orderId, order);

    return Optional.ofNullable(orderEntity)
        .flatMap(orderRepository::update)
        .map(this::toDomain);
  }

  @Override
  public Optional<Order> deleteOrder(String orderId) {

    return orderRepository.delete(orderId)
        .map(this::toDomain);
  }

  @Override
  public Optional<Order> getOrder(String orderId) {
    return orderRepository.getOrder(orderId)
        .map(this::toDomain);
  }

  private Order toDomain(OrderEntity orderEntity) {

    var shipDate = Optional.ofNullable(orderEntity.getShipDate())
        .map(date -> LocalDate.parse(date, DATETIME_FORMATTER))
        .orElse(null);

    return Order.builder()
        .id(orderEntity.getIdString())
        .orderItems(toDomain(orderEntity.getOrderItems()))
        .shipDate(shipDate)
        .status(Order.Status.from(orderEntity.getStatus()))
        .build();
  }

  private List<OrderItem> toDomain(List<OrderItemEntity> orderItemEntities) {

    return orderItemEntities.stream()
        .map(orderItemEntity -> OrderItem.builder()
            .productId(orderItemEntity.getProductId())
            .quantity(orderItemEntity.getQuantity())
            .build())
        .collect(Collectors.toList());
  }

  private OrderEntity toEntity(String orderId, Order order) {

    if (Objects.isNull(orderId) || Objects.isNull(order)) {
      return null;
    }

    var orderEntity = new OrderEntity();
    var shipDate = Optional
        .ofNullable(order.getShipDate())
        .map(localDate -> localDate.format(DATETIME_FORMATTER))
        .orElse(null);

    orderEntity.setId(new ObjectId(orderId));
    orderEntity.setShipDate(shipDate);
    orderEntity.setStatus(order.getStatusName());

    return orderEntity;
  }

  private List<OrderItemEntity> toEntity(List<OrderItem> orderItems) {

    return orderItems.stream()
        .map(orderItem -> {
          var orderItemEntity = new OrderItemEntity();
          orderItemEntity.setProductId(orderItem.getProductId());
          orderItemEntity.setQuantity(orderItem.getQuantity());

          return orderItemEntity;
        })
        .collect(Collectors.toList());
  }
}
