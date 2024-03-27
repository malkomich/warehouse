package com.acrolinx.core;

import com.acrolinx.core.domain.Product;
import com.acrolinx.db.ProductRepository;
import com.acrolinx.db.entity.ProductEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.mock;

public class ProductServiceTest {

  private static final ProductRepository productRepository = mock(ProductRepository.class);

  private ProductService productService;

  @BeforeEach
  void setup() {
    productService = new ProductService(productRepository);
  }

  @Test
  @DisplayName("Verify an empty result when the product is not found")
  public void productNotFound() {

    Mockito.when(productRepository.getProduct("123abc123abc123abc123abc")).thenReturn(Optional.empty());

    var result = productService.getProductById("123abc123abc123abc123abc");

    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("Verify the product is successfully retrieved")
  public void productRetrieved() {

    var productEntity = Mockito.mock(ProductEntity.class);

    Mockito.when(productRepository.getProduct("123abc123abc123abc123abc")).thenReturn(Optional.of(productEntity));
    Mockito.when(productEntity.getName()).thenReturn("Furniture");

    var result = productService.getProductById("123abc123abc123abc123abc");

    Assertions.assertEquals("Furniture", result.get().getName());
  }

  @Test
  @DisplayName("Verify the product filter is returning a product")
  public void productFilterEmpty() {

    Mockito.when(productRepository.getProductsByTags(Collections.singletonList("tag")))
        .thenReturn(Collections.emptyList());

    var result = productService.filterProductsByTags(Collections.singletonList("tag"));

    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("Verify the product filter is returning a product")
  public void productFilter() {

    var productEntity = Mockito.mock(ProductEntity.class);

    Mockito.when(productRepository.getProductsByTags(Collections.singletonList("tag")))
        .thenReturn(Collections.singletonList(productEntity));
    Mockito.when(productEntity.getName()).thenReturn("Furniture");

    var result = productService.filterProductsByTags(Collections.singletonList("tag"));

    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals("Furniture", result.get(0).getName());
  }
}
