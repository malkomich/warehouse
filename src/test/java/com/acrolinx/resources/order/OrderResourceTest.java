package com.acrolinx.resources.order;

import com.acrolinx.api.OrderItem;
import com.acrolinx.api.Status;
import com.acrolinx.api.request.NewOrderRequest;
import com.acrolinx.api.request.OrderUpdateRequest;
import com.acrolinx.api.response.OrderStatus;
import com.acrolinx.core.SaveOrderUseCase;
import com.acrolinx.core.domain.Order;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.mock;

@ExtendWith(DropwizardExtensionsSupport.class)
class OrderResourceTest {

  private static final SaveOrderUseCase saveOrderUseCase = mock(SaveOrderUseCase.class);
  private static final ResourceExtension RESOURCE = ResourceExtension.builder()
      .addResource(new OrderResource(saveOrderUseCase))
      .build();

  @AfterEach
  void tearDown() {
    Mockito.reset(saveOrderUseCase);
  }

  @Test
  @DisplayName("Fail when the order contains invalid product ids")
  void orderWithInvalidProducts() {

    var orderItem = new OrderItem("1", 10);
    var orderRequest = new NewOrderRequest(Collections.singletonList(orderItem));
    var entity = Entity.entity(orderRequest, MediaType.APPLICATION_JSON_TYPE);

    var response = RESOURCE.target("/order")
        .request()
        .post(entity);

    Assertions.assertEquals(422, response.getStatus());
  }

  @Test
  @DisplayName("Fail when an order item has invalid quantity")
  void orderWithInvalidQuantity() {

    var orderItem = new OrderItem("123abc123abc123abc123abc", 0);
    var orderRequest = new NewOrderRequest(Collections.singletonList(orderItem));
    var entity = Entity.entity(orderRequest, MediaType.APPLICATION_JSON_TYPE);

    var response = RESOURCE.target("/order")
        .request()
        .post(entity);

    Assertions.assertEquals(422, response.getStatus());
  }

  @Test
  @DisplayName("Verifies an order is successfully executed")
  void orderSuccessful() {

    var orderItem = new OrderItem("123abc123abc123abc123abc", 10);
    var orderRequest = new NewOrderRequest(Collections.singletonList(orderItem));
    var entity = Entity.entity(orderRequest, MediaType.APPLICATION_JSON_TYPE);

    var argumentCaptor = ArgumentCaptor.forClass(Order.class);

    Mockito.when(saveOrderUseCase.createOrder(argumentCaptor.capture()))
        .thenReturn(Optional.of("123abc123abc123abc123abc"));


    var response = RESOURCE.target("/order")
        .request()
        .post(entity);

    Assertions.assertEquals(201, response.getStatus());
    Assertions.assertTrue(response.getHeaderString("Location").contains("/order/123abc123abc123abc123abc"));
  }

  @Test
  @DisplayName("Fail when the order ID given is invalid")
  void orderIdInvalid() {

    var response = RESOURCE.target("/order/1")
        .request()
        .get();

    Assertions.assertEquals(400, response.getStatus());
  }

  @Test
  @DisplayName("Verifies there is no order with the given ID")
  void orderNotFound() {

    Mockito.when(saveOrderUseCase.getOrder("123abc123abc123abc123abc")).thenReturn(Optional.empty());

    var response = RESOURCE.target("/order/123abc123abc123abc123abc")
        .request()
        .get();

    Assertions.assertEquals(404, response.getStatus());
  }

  @Test
  @DisplayName("Verifies the order status is successfully retrieved")
  void orderFound() {

    var order = Mockito.mock(Order.class);

    Mockito.when(saveOrderUseCase.getOrder("123abc123abc123abc123abc")).thenReturn(Optional.of(order));

    var invocation = RESOURCE.target("/order/123abc123abc123abc123abc")
        .request()
        .buildGet();

    Assertions.assertEquals(200, invocation.invoke().getStatus());
    Assertions.assertNotNull(invocation.invoke(OrderStatus.class));
  }

  @Test
  @DisplayName("Verifies the order to be updated does not exist")
  void orderNotFoundForUpdate() {

    var orderItems = Collections.singletonList(new OrderItem("123abc123abc123abc123abc", 1));
    var orderUpdateRequest = new OrderUpdateRequest("2024-03-27", orderItems, Status.DELIVERED);
    var entity = Entity.entity(orderUpdateRequest, MediaType.APPLICATION_JSON_TYPE);

    Mockito
        .when(saveOrderUseCase.updateOrder(
            ArgumentMatchers.eq("123abc123abc123abc123abc"),
            ArgumentMatchers.any(Order.class)))
        .thenReturn(Optional.empty());

    var response = RESOURCE.target("/order/123abc123abc123abc123abc")
        .request()
        .put(entity);

    Assertions.assertEquals(404, response.getStatus());
  }

  @Test
  @DisplayName("Verifies an order update is successfully executed")
  void orderUpdateSuccessful() {

    var orderItems = Collections.singletonList(new OrderItem("123abc123abc123abc123abc", 1));
    var orderUpdateRequest = new OrderUpdateRequest("2024-03-27", orderItems, Status.DELIVERED);
    var entity = Entity.entity(orderUpdateRequest, MediaType.APPLICATION_JSON_TYPE);
    var order = Mockito.mock(Order.class);

    var argumentCaptor = ArgumentCaptor.forClass(Order.class);

    Mockito
        .when(saveOrderUseCase.updateOrder(
            ArgumentMatchers.eq("123abc123abc123abc123abc"),
            argumentCaptor.capture()))
        .thenReturn(Optional.of(order));

    var invocation = RESOURCE.target("/order/123abc123abc123abc123abc")
        .request()
        .buildPut(entity);

    Assertions.assertEquals(200, invocation.invoke().getStatus());
    Assertions.assertNotNull(invocation.invoke(OrderStatus.class));
    Assertions.assertEquals(Order.Status.DELIVERED, argumentCaptor.getValue().getStatus());
  }

  @Test
  @DisplayName("Fail when the order to be deleted does not exist")
  void deleteOrderIdInvalid() {

    var response = RESOURCE.target("/order/1")
        .request()
        .delete();

    Assertions.assertEquals(400, response.getStatus());
  }

  @Test
  @DisplayName("Verifies the order to be deleted does not exist")
  void deleteOrderNotFound() {

    Mockito.when(saveOrderUseCase.deleteOrder("123abc123abc123abc123abc")).thenReturn(Optional.empty());

    var response = RESOURCE.target("/order/123abc123abc123abc123abc")
        .request()
        .delete();

    Assertions.assertEquals(404, response.getStatus());
  }

  @Test
  @DisplayName("Verifies the order has been successfully deleted")
  void orderDeleted() {

    var order = Mockito.mock(Order.class);
    Mockito.when(saveOrderUseCase.deleteOrder("123abc123abc123abc123abc")).thenReturn(Optional.of(order));

    var response = RESOURCE.target("/order/123abc123abc123abc123abc")
        .request()
        .delete();

    Assertions.assertEquals(204, response.getStatus());
  }

}
