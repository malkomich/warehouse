package com.acrolinx.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderItemTest {

  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

  @Test
  void seralizesToJSON() throws Exception {

    var orderItem = new OrderItem(1, 10);

    final String expected = MAPPER.writeValueAsString(
        MAPPER.readValue(getClass().getResource("/fixtures/api/orderItem.json"), OrderItem.class));

    Assertions.assertEquals(expected, MAPPER.writeValueAsString(orderItem));
  }
}
