package com.acrolinx.core;

import com.acrolinx.core.domain.Order;
import com.acrolinx.db.OrderRepository;
import com.acrolinx.db.entity.OrderEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.Mockito.mock;

class OrderServiceTest {

  private static final OrderRepository orderRepository = mock(OrderRepository.class);

  private OrderService orderService;

  @BeforeEach
  void setup() {
    orderService = new OrderService(orderRepository);
  }

  @Test
  @DisplayName("Verify the order is not found")
  void orderNotFound() {

    Mockito.when(orderRepository.getOrder("123abc123abc123abc123abc")).thenReturn(Optional.empty());

    var result = orderService.getOrder("123abc123abc123abc123abc");

    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("Verify the order cannot be serialized with a wrong status")
  void orderInvalidStatus() {

    var orderEntity = Mockito.mock(OrderEntity.class);

    Mockito.when(orderRepository.getOrder("123abc123abc123abc123abc")).thenReturn(Optional.of(orderEntity));
    Mockito.when(orderEntity.getStatus()).thenReturn("INVALID");

    var result = orderService.getOrder("123abc123abc123abc123abc");

    Assertions.assertNull(result.get().getStatus());
  }

  @Test
  @DisplayName("Verify the order is successfully found")
  void orderFound() {

    var orderEntity = Mockito.mock(OrderEntity.class);

    Mockito.when(orderRepository.getOrder("123abc123abc123abc123abc")).thenReturn(Optional.of(orderEntity));
    Mockito.when(orderEntity.getStatus()).thenReturn("PLACED");

    var result = orderService.getOrder("123abc123abc123abc123abc");

    Assertions.assertEquals(Order.Status.PLACED, result.get().getStatus());
  }

  @Test
  @DisplayName("Verify the order id is not retrieved when the creation is not acknowledged")
  void orderCreateIsNotAcknowledge() {

    var order = Mockito.mock(Order.class);

    Mockito.when(orderRepository.createOrder(ArgumentMatchers.any(OrderEntity.class)))
        .thenReturn(Optional.empty());

    var result = orderService.createOrder(order);

    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("Verify the order created status is always set as PLACED")
  void orderCreatedStatusIsPlaced() {

    var order = Mockito.mock(Order.class);
    var orderEntityCaptor = ArgumentCaptor.forClass(OrderEntity.class);

    Mockito.when(orderRepository.createOrder(orderEntityCaptor.capture()))
        .thenReturn(Optional.of("123abc123abc123abc123abc"));
    Mockito.when(order.getStatus()).thenReturn(Order.Status.DELIVERED);

    orderService.createOrder(order);

    Assertions.assertEquals("PLACED", orderEntityCaptor.getValue().getStatus());
  }

  @Test
  @DisplayName("Verify the order is successfully created")
  void orderCreated() {

    var order = Mockito.mock(Order.class);

    Mockito.when(orderRepository.createOrder(ArgumentMatchers.any(OrderEntity.class)))
        .thenReturn(Optional.of("123abc123abc123abc123abc"));

    var result = orderService.createOrder(order);

    Assertions.assertEquals("123abc123abc123abc123abc", result.get());
  }

  @Test
  @DisplayName("The order to be partial updated does not exist")
  void orderPartialUpdateNotExist() {

    var order = Mockito.mock(Order.class);

    Mockito.when(orderRepository.update(ArgumentMatchers.any(OrderEntity.class))).thenReturn(Optional.empty());

    var result = orderService.partialUpdateOrder("123abc123abc123abc123abc", order);

    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("Get an empty response when the id for the order to be partial updated is missing")
  void orderPartialUpdateWithoutOrderId() {

    var order = Mockito.mock(Order.class);

    var result = orderService.partialUpdateOrder(null, order);

    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("Get an empty response when the order to be partial updated is missing")
  void orderPartialUpdateEmpty() {

    var result = orderService.partialUpdateOrder("123abc123abc123abc123abc", null);

    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("Verify the order is successfully partial updated")
  void orderPartialUpdated() {

    var order = Mockito.mock(Order.class);
    var orderEntity = Mockito.mock(OrderEntity.class);
    var argumentCaptor = ArgumentCaptor.forClass(OrderEntity.class);

    Mockito.when(orderRepository.update(argumentCaptor.capture()))
        .thenReturn(Optional.of(orderEntity));

    var result = orderService.partialUpdateOrder("123abc123abc123abc123abc", order);

    Assertions.assertEquals("123abc123abc123abc123abc", argumentCaptor.getValue().getIdString());
  }

  @Test
  @DisplayName("The order to be updated does not exist")
  void orderUpdateNotExist() {

    var order = Mockito.mock(Order.class);

    Mockito.when(orderRepository.replace(ArgumentMatchers.any(OrderEntity.class))).thenReturn(Optional.empty());

    var result = orderService.updateOrder("123abc123abc123abc123abc", order);

    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("Get an empty response when the id for the order to be updated is missing")
  void orderUpdateWithoutOrderId() {

    var order = Mockito.mock(Order.class);

    var result = orderService.updateOrder(null, order);

    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("Get an empty response when the order to be updated is missing")
  void orderUpdateEmpty() {

    var result = orderService.updateOrder("123abc123abc123abc123abc", null);

    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("Verify the order is successfully updated")
  void orderUpdated() {

    var order = Mockito.mock(Order.class);
    var orderEntity = Mockito.mock(OrderEntity.class);
    var argumentCaptor = ArgumentCaptor.forClass(OrderEntity.class);

    Mockito.when(orderRepository.replace(argumentCaptor.capture()))
        .thenReturn(Optional.of(orderEntity));

    var result = orderService.updateOrder("123abc123abc123abc123abc", order);

    Assertions.assertEquals("123abc123abc123abc123abc", argumentCaptor.getValue().getIdString());
  }

  @Test
  @DisplayName("The order to be deleted does not exist")
  void orderDeleteNotExist() {

    Mockito.when(orderRepository.delete("123abc123abc123abc123abc")).thenReturn(Optional.empty());

    var result = orderService.deleteOrder("123abc123abc123abc123abc");

    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("Verify the order is successfully deleted")
  void orderDeleted() {

    var orderEntity = Mockito.mock(OrderEntity.class);

    Mockito.when(orderRepository.delete("123abc123abc123abc123abc")).thenReturn(Optional.of(orderEntity));
    Mockito.when(orderEntity.getStatus()).thenReturn("APPROVED");

    var result = orderService.deleteOrder("123abc123abc123abc123abc");

    Assertions.assertEquals(Order.Status.APPROVED, result.get().getStatus());
  }
}
