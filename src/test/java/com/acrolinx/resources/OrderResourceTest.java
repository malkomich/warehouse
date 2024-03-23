package com.acrolinx.resources;

import com.acrolinx.api.OrderItem;
import com.acrolinx.api.request.OrderRequest;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;

@ExtendWith(DropwizardExtensionsSupport.class)
public class OrderResourceTest {

  private static final ResourceExtension RESOURCE = ResourceExtension.builder()
      .addResource(new ProductResource())
      .build();

  @Test
  @DisplayName("Fail when the order contains invalid product ids")
  public void orderWithInvalidProducts() {

    var orderItem = new OrderItem(-1, 10);
    var orderRequest = new OrderRequest(Collections.singletonList(orderItem));
    var entity = Entity.entity(orderRequest, MediaType.APPLICATION_JSON_TYPE);

    var response = RESOURCE.target("/order").request().post(entity);

    Assertions.assertEquals(400, response.getStatus());
    Assertions.assertNull(response.getEntity());
  }

  @Test
  @DisplayName("Fail when an order item has invalid quantity")
  public void orderWithInvalidQuantity() {

    var orderItem = new OrderItem(1, 0);
    var orderRequest = new OrderRequest(Collections.singletonList(orderItem));
    var entity = Entity.entity(orderRequest, MediaType.APPLICATION_JSON_TYPE);

    var response = RESOURCE.target("/order").request().post(entity);

    Assertions.assertEquals(400, response.getStatus());
    Assertions.assertNull(response.getEntity());
  }

  @Test
  @DisplayName("Verifies an order is successfully executed")
  public void orderSuccessful() {

    var orderItem = new OrderItem(1, 10);
    var orderRequest = new OrderRequest(Collections.singletonList(orderItem));
    var entity = Entity.entity(orderRequest, MediaType.APPLICATION_JSON_TYPE);

    var response = RESOURCE.target("/order").request().post(entity);

    Assertions.assertEquals(200, response.getStatus());
    Assertions.assertNull(response.getEntity());
  }

  @Test
  @DisplayName("Fail when the order ID given is invalid")
  public void orderIdInvalid() {

    var response = RESOURCE.target("/order/-1").request().get();

    Assertions.assertEquals(400, response.getStatus());
    Assertions.assertNull(response.getEntity());
  }

  @Test
  @DisplayName("Verifies there is no order with the given ID")
  public void orderNotFound() {

    var response = RESOURCE.target("/order/999").request().get();

    Assertions.assertEquals(404, response.getStatus());
    Assertions.assertNull(response.getEntity());
  }

  @Test
  @DisplayName("Verifies the order status is successfully retrieved")
  public void orderFound() {

    var response = RESOURCE.target("/order/1").request().get();

    Assertions.assertEquals(200, response.getStatus());
    Assertions.assertNotNull(response.getEntity());
  }

  @Test
  @DisplayName("Update failed when the order contains invalid product ids")
  public void orderUpdateWithInvalidProducts() {

    var orderItem = new OrderItem(-1, 10);
    var orderRequest = new OrderRequest(Collections.singletonList(orderItem));
    var entity = Entity.entity(orderRequest, MediaType.APPLICATION_JSON_TYPE);

    var response = RESOURCE.target("/order/-1").request().put(entity);

    Assertions.assertEquals(400, response.getStatus());
    Assertions.assertNull(response.getEntity());
  }

  @Test
  @DisplayName("Update failed when an order item has invalid quantity")
  public void orderUpdateWithInvalidQuantity() {

    var orderItem = new OrderItem(1, 0);
    var orderRequest = new OrderRequest(Collections.singletonList(orderItem));
    var entity = Entity.entity(orderRequest, MediaType.APPLICATION_JSON_TYPE);

    var response = RESOURCE.target("/order/1").request().put(entity);

    Assertions.assertEquals(400, response.getStatus());
    Assertions.assertNull(response.getEntity());
  }

  @Test
  @DisplayName("Verifies the order to be updated does not exist")
  public void orderNotFoundForUpdate() {

    var orderItem = new OrderItem(999, 0);
    var orderRequest = new OrderRequest(Collections.singletonList(orderItem));
    var entity = Entity.entity(orderRequest, MediaType.APPLICATION_JSON_TYPE);

    var response = RESOURCE.target("/order/999").request().put(entity);

    Assertions.assertEquals(404, response.getStatus());
    Assertions.assertNull(response.getEntity());
  }

  @Test
  @DisplayName("Verifies an order update is successfully executed")
  public void orderUpdateSuccessful() {

    var orderItem = new OrderItem(1, 10);
    var orderRequest = new OrderRequest(Collections.singletonList(orderItem));
    var entity = Entity.entity(orderRequest, MediaType.APPLICATION_JSON_TYPE);

    var response = RESOURCE.target("/order/1").request().put(entity);

    Assertions.assertEquals(200, response.getStatus());
    Assertions.assertNull(response.getEntity());
  }

  @Test
  @DisplayName("Fail when the order to be deleted does not exist")
  public void deleteOrderIdInvalid() {

    var response = RESOURCE.target("/order/-1").request().delete();

    Assertions.assertEquals(400, response.getStatus());
  }

  @Test
  @DisplayName("Verifies the order to be deleted does not exist")
  public void deleteOrderNotFound() {

    var response = RESOURCE.target("/order/999").request().delete();

    Assertions.assertEquals(404, response.getStatus());
  }

  @Test
  @DisplayName("Verifies the order has been successfully deleted")
  public void orderDeleted() {

    var response = RESOURCE.target("/order/1").request().delete();

    Assertions.assertEquals(200, response.getStatus());
  }

}
