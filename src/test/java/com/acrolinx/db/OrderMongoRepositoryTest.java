package com.acrolinx.db;

import com.acrolinx.db.entity.OrderEntity;
import com.acrolinx.db.entity.OrderItemEntity;
import com.mongodb.MongoClientException;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

class OrderMongoRepositoryTest {

  private static TestDbContainer testDbContainer;
  private static OrderMongoRepository orderRepository;

  @BeforeEach
  void setUp() {
    testDbContainer = new TestDbContainer();
    testDbContainer.start();

    var collection = testDbContainer
        .getDatabase()
        .getCollection(OrderMongoRepository.COLLECTION_NAME, OrderEntity.class);

    orderRepository = new OrderMongoRepository(collection);
  }

  @AfterEach
  void tearDown() {
    testDbContainer.stop();
  }

  @Test
  @DisplayName("Verify the order is not found in DB")
  void orderNotFound() {

    var result = orderRepository.getOrder("999999999999999999999999");

    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("Fail when DB is down")
  void databaseDown() {

    testDbContainer.stop();

    Assertions.assertThrows(MongoClientException.class,
        () -> orderRepository.getOrder("617d673dd59fd40c61b1b370"));
  }

  @Test
  @DisplayName("Verify the order is successfully retrieved")
  void getOrder() {

    var result = orderRepository.getOrder("617d673dd59fd40c61b1b370");

    Assertions.assertTrue(result.isPresent());
    Assertions.assertEquals("2024-03-29", result.get().getShipDate());
  }

  @Test
  @DisplayName("Verify the order is successfully created")
  void createOrder() {

    var orderEntity = Mockito.mock(OrderEntity.class);
    var orderItemEntity = Mockito.mock(OrderItemEntity.class);

    Mockito.when(orderEntity.getOrderItems()).thenReturn(Collections.singletonList(orderItemEntity));
    Mockito.when(orderEntity.getStatus()).thenReturn("PLACED");
    Mockito.when(orderItemEntity.getProductId()).thenReturn("abc123abc123abc123abc123");
    Mockito.when(orderItemEntity.getQuantity()).thenReturn(5);

    var result = orderRepository.createOrder(orderEntity);

    Assertions.assertTrue(result.isPresent());

    var order = orderRepository.getOrder(result.get());

    Assertions.assertTrue(order.isPresent());
    Assertions.assertEquals("PLACED", order.get().getStatus());
    Assertions.assertEquals("abc123abc123abc123abc123", order.get().getOrderItems().get(0).getProductId());
    Assertions.assertEquals(5, order.get().getOrderItems().get(0).getQuantity());
  }

  @Test
  @DisplayName("Verify the order cannot be replaced if it does not exist")
  void replaceOrderNotFound() {

    var orderEntity = Mockito.mock(OrderEntity.class);

    Mockito.when(orderEntity.getId()).thenReturn(new ObjectId("999999999999999999999999"));
    Mockito.when(orderEntity.getStatus()).thenReturn("DELIVERED");

    var result = orderRepository.replace(orderEntity);

    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("Verify the order is successfully replaced")
  void replaceOrder() {

    var orderEntity = Mockito.mock(OrderEntity.class);

    Mockito.when(orderEntity.getId()).thenReturn(new ObjectId("617d673dd59fd40c61b1b370"));
    Mockito.when(orderEntity.getShipDate()).thenReturn("2030-03-29");
    Mockito.when(orderEntity.getStatus()).thenReturn("DELIVERED");

    var result = orderRepository.replace(orderEntity);

    Assertions.assertEquals("2030-03-29", result.get().getShipDate());
    Assertions.assertTrue(result.get().getOrderItems().isEmpty());
    Assertions.assertEquals("DELIVERED", result.get().getStatus());
  }

  @Test
  @DisplayName("Verify the order cannot be updated if it does not exist")
  void updateOrderNotFound() {

    var orderEntity = Mockito.mock(OrderEntity.class);

    Mockito.when(orderEntity.getId()).thenReturn(new ObjectId("999999999999999999999999"));
    Mockito.when(orderEntity.getStatus()).thenReturn("DELIVERED");

    var result = orderRepository.update(orderEntity);

    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("Verify the order is successfully updated")
  void updateOrder() {

    var orderEntity = Mockito.mock(OrderEntity.class);

    Mockito.when(orderEntity.getId()).thenReturn(new ObjectId("617d673dd59fd40c61b1b370"));
    Mockito.when(orderEntity.getStatus()).thenReturn("DELIVERED");

    var result = orderRepository.update(orderEntity);

    Assertions.assertEquals("2024-03-29", result.get().getShipDate());
    Assertions.assertEquals("DELIVERED", result.get().getStatus());
  }

  @Test
  @DisplayName("Verify the order cannot be deleted if does not exist")
  void deleteOrderNotFound() {

    var result = orderRepository.delete("999999999999999999999999");

    Assertions.assertFalse(result.isPresent());
  }

  @Test
  @DisplayName("Verify the order is successfully deleted")
  void deleteOrder() {

    var result = orderRepository.delete("617d673dd59fd40c61b1b370");

    Assertions.assertTrue(result.isPresent());
    Assertions.assertFalse(orderRepository.getOrder("617d673dd59fd40c61b1b370").isPresent());
  }
}
