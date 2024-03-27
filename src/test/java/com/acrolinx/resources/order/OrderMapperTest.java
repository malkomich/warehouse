package com.acrolinx.resources.order;

import com.acrolinx.api.OrderItem;
import com.acrolinx.api.Status;
import com.acrolinx.api.request.NewOrderRequest;
import com.acrolinx.api.request.OrderPartialUpdateRequest;
import com.acrolinx.api.request.OrderUpdateRequest;
import com.acrolinx.core.domain.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

class OrderMapperTest {

  @Test
  void successfulMappingNewOrder() {

    var newOrderRequest = Mockito.mock(NewOrderRequest.class);

    Mockito.when(newOrderRequest.getOrderItems())
        .thenReturn(Collections.singletonList(new OrderItem("123abc123abc123abc123abc", 10)));

    var result = OrderMapper.toOrder(newOrderRequest);

    Assertions.assertEquals("123abc123abc123abc123abc", result.getOrderItems().get(0).getProductId());
    Assertions.assertEquals(10, result.getOrderItems().get(0).getQuantity());
  }

  @Test
  void successfulMappingPartialUpdateRequest() {

    var orderPartialUpdateRequest = Mockito.mock(OrderPartialUpdateRequest.class);

    Mockito.when(orderPartialUpdateRequest.getShipDate()).thenReturn("2024-03-12");
    Mockito.when(orderPartialUpdateRequest.getStatus()).thenReturn(Status.APPROVED);

    var result = OrderMapper.toOrder(orderPartialUpdateRequest);

    Assertions.assertEquals(Order.Status.APPROVED, result.getStatus());
    Assertions.assertEquals(2024, result.getShipDate().getYear());
    Assertions.assertEquals(3, result.getShipDate().getMonthValue());
    Assertions.assertEquals(12, result.getShipDate().getDayOfMonth());
  }

  @Test
  void successfulMappingUpdateRequest() {

    var orderUpdateRequest = Mockito.mock(OrderUpdateRequest.class);

    Mockito.when(orderUpdateRequest.getShipDate()).thenReturn("2024-03-12");
    Mockito.when(orderUpdateRequest.getStatus()).thenReturn(Status.APPROVED);
    Mockito.when(orderUpdateRequest.getOrderItems())
        .thenReturn(Collections.singletonList(new OrderItem("123abc123abc123abc123abc", 10)));

    var result = OrderMapper.toOrder(orderUpdateRequest);

    Assertions.assertEquals(Order.Status.APPROVED, result.getStatus());
    Assertions.assertEquals(2024, result.getShipDate().getYear());
    Assertions.assertEquals(3, result.getShipDate().getMonthValue());
    Assertions.assertEquals(12, result.getShipDate().getDayOfMonth());
    Assertions.assertEquals("123abc123abc123abc123abc", result.getOrderItems().get(0).getProductId());
    Assertions.assertEquals(10, result.getOrderItems().get(0).getQuantity());
  }
}
