package com.acrolinx.resources.product;

import com.acrolinx.api.response.ProductInfo;
import com.acrolinx.core.FilterProductsUseCase;
import com.acrolinx.core.GetProductUseCase;
import com.acrolinx.core.domain.Product;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;

@ExtendWith(DropwizardExtensionsSupport.class)
class ProductResourceTest {

  private static final GetProductUseCase getProductUseCase = mock(GetProductUseCase.class);
  private static final FilterProductsUseCase filterProductsUseCase = mock(FilterProductsUseCase.class);
  private static final StatefulRedisConnection statefulRedisConnection = mock(StatefulRedisConnection.class);
  private static final ResourceExtension RESOURCE = ResourceExtension.builder()
      .addResource(new ProductResource(getProductUseCase, filterProductsUseCase, statefulRedisConnection))
      .build();

  @AfterEach
  void tearDown() {
    Mockito.reset(getProductUseCase);
    Mockito.reset(filterProductsUseCase);
    Mockito.reset(statefulRedisConnection);
  }

  @Test
  @DisplayName("Verifies there is no product with the given ID")
  void productNotFound() {

    Mockito.when(getProductUseCase.getProductById("123abc123abc123abc123abc")).thenReturn(Optional.empty());

    var response = RESOURCE.target("/product/123abc123abc123abc123abc")
        .request()
        .get();

    Assertions.assertEquals(404, response.getStatus());
  }

  @Test
  @DisplayName("Fail when the product ID given is invalid")
  void productIdInvalid() {

    var response = RESOURCE.target("/product/1")
        .request()
        .get();

    Assertions.assertEquals(400, response.getStatus());
  }

  @Test
  @DisplayName("Verifies the product is successful found and retrieved")
  void productFound() {

    var product = Mockito.mock(Product.class);
    Mockito.when(getProductUseCase.getProductById("123abc123abc123abc123abc")).thenReturn(Optional.of(product));

    var invocation = RESOURCE.target("/product/123abc123abc123abc123abc")
        .request()
        .buildGet();

    Assertions.assertEquals(200, invocation.invoke().getStatus());
    Assertions.assertNotNull(invocation.invoke(ProductInfo.class));
  }

  @Test
  @DisplayName("Verifies the product is successful found in cache")
  void productFoundInCache() {

    var redisCommands = Mockito.mock(RedisCommands.class);

    Mockito.when(statefulRedisConnection.sync()).thenReturn(redisCommands);
    Mockito.when(redisCommands.get("123abc123abc123abc123abc"))
        .thenReturn("{\"id\":\"123abc123abc123abc123abc\",\"name\":\"Furniture\",\"tags\":[],\"quantity\":10}");

    var invocation = RESOURCE.target("/product/123abc123abc123abc123abc")
        .request()
        .buildGet();

    Assertions.assertEquals(200, invocation.invoke().getStatus());
    Mockito.verifyNoInteractions(getProductUseCase);
    Assertions.assertEquals("Furniture", invocation.invoke(ProductInfo.class).getName());
  }

  @Test
  @DisplayName("Fail when no tags are provided in the filter operation")
  void filterWithoutTags() {

    var response = RESOURCE.target("/product/filter").request().get();

    Assertions.assertEquals(400, response.getStatus());
  }

  @Test
  @DisplayName("Verifies there are no products with the given tags")
  void filterNoProductsFound() {

    Mockito.when(filterProductsUseCase.filterProductsByTags(Arrays.asList("purple", "vintage")))
        .thenReturn(Collections.emptyList());

    var invocation = RESOURCE.target("/product/filter")
        .queryParam("tags", "purple", "vintage")
        .request()
        .buildGet();

    Assertions.assertEquals(204, invocation.invoke().getStatus());
    Assertions.assertNull(invocation.invoke(ProductInfo.class));
  }

  @Test
  @DisplayName("Verifies the products are filtered and retrieved")
  void filterTagsProductsFound() {

    var product = Mockito.mock(Product.class);
    Mockito.when(filterProductsUseCase.filterProductsByTags(Collections.singletonList("livingroom")))
        .thenReturn(Collections.singletonList(product));

    var invocation = RESOURCE.target("/product/filter")
        .queryParam("tags", "livingroom")
        .request()
        .buildGet();

    Assertions.assertEquals(200, invocation.invoke().getStatus());
    Assertions.assertNotNull(invocation.invoke(List.class));
  }
}
