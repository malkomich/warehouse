package com.acrolinx.resources;

import com.acrolinx.api.response.ProductInfo;
import com.acrolinx.core.FilterProductsUseCase;
import com.acrolinx.core.GetProductUseCase;
import com.acrolinx.core.domain.Product;
import com.acrolinx.resources.product.ProductResource;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
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
public class ProductResourceTest {

  private static final GetProductUseCase getProductUseCase = mock(GetProductUseCase.class);
  private static final FilterProductsUseCase filterProductsUseCase = mock(FilterProductsUseCase.class);
  private static final ResourceExtension RESOURCE = ResourceExtension.builder()
      .addResource(new ProductResource(getProductUseCase, filterProductsUseCase))
      .build();

  @AfterEach
  void tearDown() {
    Mockito.reset(getProductUseCase);
    Mockito.reset(filterProductsUseCase);
  }

  @Test
  @DisplayName("Verifies there is no product with the given ID")
  public void productNotFound() {

    Mockito.when(getProductUseCase.getProductById("123abc123abc123abc123abc")).thenReturn(Optional.empty());

    var response = RESOURCE.target("/product/123abc123abc123abc123abc")
        .request()
        .get();

    Assertions.assertEquals(404, response.getStatus());
  }

  @Test
  @DisplayName("Fail when the product ID given is invalid")
  public void productIdInvalid() {

    var response = RESOURCE.target("/product/1")
        .request()
        .get();

    Assertions.assertEquals(400, response.getStatus());
  }

  @Test
  @DisplayName("Verifies the product is successful found and retrieved")
  public void productFound() {

    var product = Mockito.mock(Product.class);
    Mockito.when(getProductUseCase.getProductById("123abc123abc123abc123abc")).thenReturn(Optional.of(product));

    var invocation = RESOURCE.target("/product/123abc123abc123abc123abc")
        .request()
        .buildGet();

    Assertions.assertEquals(200, invocation.invoke().getStatus());
    Assertions.assertNotNull(invocation.invoke(ProductInfo.class));
  }

  @Test
  @DisplayName("Fail when no tags are provided in the filter operation")
  public void filterWithoutTags() {

    var response = RESOURCE.target("/product/filter").request().get();

    Assertions.assertEquals(400, response.getStatus());
  }

  @Test
  @DisplayName("Verifies there are no products with the given tags")
  public void filterNoProductsFound() {

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
  public void filterTagsProductsFound() {

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
