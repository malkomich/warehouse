package com.acrolinx.resources.product;

import com.acrolinx.core.domain.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ProductMapperTest {

  @Test
  void successfulMapping() {

    var product = Mockito.mock(Product.class);

    Mockito.when(product.getName()).thenReturn("Furniture");
    Mockito.when(product.getQuantity()).thenReturn(10);

    var result = ProductMapper.toProductInfo(product);

    Assertions.assertEquals("Furniture", result.getName());
    Assertions.assertEquals(10, result.getQuantity());
  }
}
