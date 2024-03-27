package com.acrolinx.api.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

class FilterResultTest {

  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

  @Test
  void seralizesToJSON() throws Exception {

    var tags = Arrays.asList("sofa", "livingRoom", "grey");
    var productInfo = new ProductInfo("123abc123abc123abc123abc", "Fabric chaise lounge grey sofa", tags, 10);
    var filterResult = new FilterResult(Collections.singletonList(productInfo));

    final String expected = MAPPER.writeValueAsString(
        MAPPER.readValue(getClass().getResource("/fixtures/api/filterResult.json"), FilterResult.class));

    Assertions.assertEquals(expected, MAPPER.writeValueAsString(filterResult));
  }
}
