package com.acrolinx.db;

import com.acrolinx.db.entity.ProductEntity;
import com.mongodb.MongoClientException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class ProductMongoRepositoryTest {

  private static TestDbContainer testDbContainer;
  private static ProductMongoRepository productRepository;

  @BeforeEach
  void setUp() {
    testDbContainer = new TestDbContainer();
    testDbContainer.start(); // Please make sure Docker is up & running to run these integration tests.

    var collection = testDbContainer
        .getDatabase()
        .getCollection(ProductMongoRepository.COLLECTION_NAME, ProductEntity.class);

    productRepository = new ProductMongoRepository(collection);
  }

  @AfterEach
  void tearDown() {
    testDbContainer.stop();
  }

  @Test
  @DisplayName("Verify the product is not found in DB")
  void productNotFound() {

    var result = productRepository.getProduct("999999999999999999999999");

    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("Fail when DB is down")
  void databaseDown() {

    testDbContainer.stop();

    Assertions.assertThrows(MongoClientException.class,
        () -> productRepository.getProduct("617d673dd59fd40c61b1b370"));
  }

  @Test
  @DisplayName("Verify the product is successfully retrieved")
  void getProduct() {

    var result = productRepository.getProduct("617d673dd59fd40c61b1b371");

    Assertions.assertTrue(result.isPresent());
    Assertions.assertEquals("Fabric chaise lounge grey sofa", result.get().getName());
  }

  @Test
  @DisplayName("Verify there are no products with given tags")
  void filterProductsNotFound() {

    var result = productRepository.getProductsByTags(Collections.singletonList("red"));

    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("Verify the products are successfully filtered")
  void filterProducts() {

    var result = productRepository.getProductsByTags(Collections.singletonList("grey"));

    Assertions.assertFalse(result.isEmpty());
    Assertions.assertEquals("Fabric chaise lounge grey sofa", result.get(0).getName());
  }
}
