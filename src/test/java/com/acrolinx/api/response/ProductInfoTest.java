package com.acrolinx.api.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class ProductInfoTest {

  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

  @Test
  void seralizesToJSON() throws Exception {

    var tags = Arrays.asList("sofa", "livingRoom", "grey");
    var productInfo = new ProductInfo("123abc123abc123abc123abc", "Fabric chaise lounge grey sofa", tags, 10);

    final String expected = MAPPER.writeValueAsString(
        MAPPER.readValue(getClass().getResource("/fixtures/api/productInfo.json"), ProductInfo.class));

    Assertions.assertEquals(expected, MAPPER.writeValueAsString(productInfo));
  }
}
