package com.acrolinx.resources;

import com.acrolinx.api.OrderItem;
import com.acrolinx.api.request.OrderRequest;
import com.acrolinx.api.response.OrderStatus;
import com.acrolinx.core.SaveOrderUseCase;
import com.acrolinx.core.domain.Order;
import com.acrolinx.resources.order.OrderResource;
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
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Or;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.mock;

@ExtendWith(DropwizardExtensionsSupport.class)
public class OrderResourceTest {

  private static final SaveOrderUseCase saveOrderUseCase = mock(SaveOrderUseCase.class);
  private static final ResourceExtension RESOURCE = ResourceExtension.builder()
      .addResource(new OrderResource(saveOrderUseCase))
      .build();

  @AfterEach
  public void tearDown() {
    Mockito.reset(saveOrderUseCase);
  }

  @Test
  @DisplayName("Fail when the order contains invalid product ids")
  public void orderWithInvalidProducts() {

    var order = Mockito.mock(Order.class);
    Mockito.when(saveOrderUseCase.createOrder(order)).thenReturn(order);

    var orderItem = new OrderItem(-1, 10);
    var orderRequest = new OrderRequest(Collections.singletonList(orderItem));
    var entity = Entity.entity(orderRequest, MediaType.APPLICATION_JSON_TYPE);

    var response = RESOURCE.target("/order")
        .request()
        .post(entity);

    Assertions.assertEquals(422, response.getStatus());
  }

  @Test
  @DisplayName("Fail when an order item has invalid quantity")
  public void orderWithInvalidQuantity() {

    var orderItem = new OrderItem(1, 0);
    var orderRequest = new OrderRequest(Collections.singletonList(orderItem));
    var entity = Entity.entity(orderRequest, MediaType.APPLICATION_JSON_TYPE);

    var response = RESOURCE.target("/order")
        .request()
        .post(entity);

    Assertions.assertEquals(422, response.getStatus());
  }

  @Test
  @DisplayName("Verifies an order is successfully executed")
  public void orderSuccessful() {

    var orderItem = new OrderItem(1, 10);
    var orderRequest = new OrderRequest(Collections.singletonList(orderItem));
    var entity = Entity.entity(orderRequest, MediaType.APPLICATION_JSON_TYPE);
    var order = Mockito.mock(Order.class);

    var argumentCaptor = ArgumentCaptor.forClass(Order.class);

    Mockito.when(saveOrderUseCase.createOrder(argumentCaptor.capture()))
        .thenReturn(order);
    Mockito.when(order.getId()).thenReturn(1);


    var invocation = RESOURCE.target("/order")
        .request()
        .buildPost(entity);

    Assertions.assertEquals(201, invocation.invoke().getStatus());
    Assertions.assertNotNull(invocation.invoke(OrderStatus.class));
  }

  @Test
  @DisplayName("Fail when the order ID given is invalid")
  public void orderIdInvalid() {

    var response = RESOURCE.target("/order/-1")
        .request()
        .get();

    Assertions.assertEquals(400, response.getStatus());
  }

  @Test
  @DisplayName("Verifies there is no order with the given ID")
  public void orderNotFound() {

    Mockito.when(saveOrderUseCase.getOrder(999)).thenReturn(Optional.empty());

    var response = RESOURCE.target("/order/999")
        .request()
        .get();

    Assertions.assertEquals(404, response.getStatus());
  }

  @Test
  @DisplayName("Verifies the order status is successfully retrieved")
  public void orderFound() {

    var order = Mockito.mock(Order.class);

    Mockito.when(saveOrderUseCase.getOrder(1)).thenReturn(Optional.of(order));

    var invocation = RESOURCE.target("/order/1")
        .request()
        .buildGet();

    Assertions.assertEquals(200, invocation.invoke().getStatus());
    Assertions.assertNotNull(invocation.invoke(OrderStatus.class));
  }

  @Test
  @DisplayName("Update failed when the order contains invalid product ids")
  public void orderUpdateWithInvalidProducts() {

    var orderItem = new OrderItem(-1, 10);
    var orderRequest = new OrderRequest(Collections.singletonList(orderItem));
    var entity = Entity.entity(orderRequest, MediaType.APPLICATION_JSON_TYPE);

    var response = RESOURCE.target("/order/1")
        .request()
        .put(entity);

    Assertions.assertEquals(422, response.getStatus());
  }

  @Test
  @DisplayName("Update failed when an order item has invalid quantity")
  public void orderUpdateWithInvalidQuantity() {

    var orderItem = new OrderItem(1, 0);
    var orderRequest = new OrderRequest(Collections.singletonList(orderItem));
    var entity = Entity.entity(orderRequest, MediaType.APPLICATION_JSON_TYPE);

    var response = RESOURCE.target("/order/1")
        .request()
        .put(entity);

    Assertions.assertEquals(422, response.getStatus());
  }

  @Test
  @DisplayName("Verifies the order to be updated does not exist")
  public void orderNotFoundForUpdate() {

    var orderItem = new OrderItem(999, 10);
    var orderRequest = new OrderRequest(Collections.singletonList(orderItem));
    var entity = Entity.entity(orderRequest, MediaType.APPLICATION_JSON_TYPE);

    Mockito.when(saveOrderUseCase.updateOrder(ArgumentMatchers.eq(1), ArgumentMatchers.any(Order.class)))
        .thenReturn(Optional.empty());

    var response = RESOURCE.target("/order/999")
        .request()
        .put(entity);

    Assertions.assertEquals(404, response.getStatus());
  }

  @Test
  @DisplayName("Verifies an order update is successfully executed")
  public void orderUpdateSuccessful() {

    var orderItem = new OrderItem(1, 10);
    var orderRequest = new OrderRequest(Collections.singletonList(orderItem));
    var entity = Entity.entity(orderRequest, MediaType.APPLICATION_JSON_TYPE);
    var order = Mockito.mock(Order.class);

    var argumentCaptor = ArgumentCaptor.forClass(Order.class);

    Mockito.when(saveOrderUseCase.updateOrder(ArgumentMatchers.eq(1), argumentCaptor.capture()))
        .thenReturn(Optional.of(order));

    var invocation = RESOURCE.target("/order/1")
        .request()
        .buildPut(entity);

    Assertions.assertEquals(200, invocation.invoke().getStatus());
    Assertions.assertNotNull(invocation.invoke(OrderStatus.class));
    Assertions.assertEquals(10, argumentCaptor.getValue().getOrderItems().get(0).getQuantity());
  }

  @Test
  @DisplayName("Fail when the order to be deleted does not exist")
  public void deleteOrderIdInvalid() {

    var response = RESOURCE.target("/order/-1")
        .request()
        .delete();

    Assertions.assertEquals(400, response.getStatus());
  }

  @Test
  @DisplayName("Verifies the order to be deleted does not exist")
  public void deleteOrderNotFound() {

    Mockito.when(saveOrderUseCase.deleteOrder(999)).thenReturn(Optional.empty());

    var response = RESOURCE.target("/order/999")
        .request()
        .delete();

    Assertions.assertEquals(404, response.getStatus());
  }

  @Test
  @DisplayName("Verifies the order has been successfully deleted")
  public void orderDeleted() {

    var order = Mockito.mock(Order.class);
    Mockito.when(saveOrderUseCase.deleteOrder(1)).thenReturn(Optional.of(order));

    var response = RESOURCE.target("/order/1")
        .request()
        .delete();

    Assertions.assertEquals(204, response.getStatus());
  }

}
