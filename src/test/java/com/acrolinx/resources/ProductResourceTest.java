package com.acrolinx.resources;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DropwizardExtensionsSupport.class)
public class ProductResourceTest {

  private static final ResourceExtension RESOURCE = ResourceExtension.builder()
      .addResource(new ProductResource())
      .build();

  @Test
  @DisplayName("Verifies there is no product with the given ID")
  public void productNotFound() {

    var response = RESOURCE.target("/product/999").request().get();

    Assertions.assertEquals(404, response.getStatus());
    Assertions.assertNull(response.getEntity());
  }

  @Test
  @DisplayName("Fail when the product ID given is invalid")
  public void productIdInvalid() {

    var response = RESOURCE.target("/product/-1").request().get();

    Assertions.assertEquals(400, response.getStatus());
    Assertions.assertNull(response.getEntity());
  }

  @Test
  @DisplayName("Verifies the product is successful found and retrieved")
  public void productFound() {

    var response = RESOURCE.target("/product/1").request().get();

    Assertions.assertEquals(200, response.getStatus());
    Assertions.assertNotNull(response.getEntity());
  }

  @Test
  @DisplayName("Fail when no tags are provided in the filter operation")
  public void filterWithoutTags() {

    var response = RESOURCE.target("/product/filter").request().get();

    Assertions.assertEquals(400, response.getStatus());
    Assertions.assertNull(response.getEntity());
  }

  @Test
  @DisplayName("Verifies there are no products with the given tags")
  public void filterNoProductsFound() {

    var response = RESOURCE.target("/product/filter?tags=purple,vintage").request().get();

    Assertions.assertEquals(204, response.getStatus());
    Assertions.assertNotNull(response.getEntity());
  }

  @Test
  @DisplayName("Verifies the products are filtered and retrieved")
  public void filterTagsProductsFound() {

    var response = RESOURCE.target("/product/filter?tags=livingroom").request().get();

    Assertions.assertEquals(200, response.getStatus());
    Assertions.assertNotNull(response.getEntity());
  }
}
