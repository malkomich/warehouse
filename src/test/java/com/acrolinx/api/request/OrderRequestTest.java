package com.acrolinx.api.request;

import com.acrolinx.api.OrderItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class OrderRequestTest {

  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

  @Test
  void seralizesToJSON() throws Exception {

    var orderItem = new OrderItem(1, 10);
    var orderRequest = new OrderRequest(Collections.singletonList(orderItem));

    final String expected = MAPPER.writeValueAsString(
        MAPPER.readValue(getClass().getResource("/fixtures/api/orderRequest.json"), OrderRequest.class));

    Assertions.assertEquals(expected, MAPPER.writeValueAsString(orderRequest));
  }
}
