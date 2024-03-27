package com.acrolinx.api.request;

import com.acrolinx.api.OrderItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class NewOrderRequestTest {

  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

  @Test
  void seralizesToJSON() throws Exception {

    var orderItem = new OrderItem("123abc123abc123abc123abc", 10);
    var orderRequest = new NewOrderRequest(Collections.singletonList(orderItem));

    final String expected = MAPPER.writeValueAsString(
        MAPPER.readValue(
            getClass().getResource("/fixtures/api/newOrderRequest.json"),
            NewOrderRequest.class));

    Assertions.assertEquals(expected, MAPPER.writeValueAsString(orderRequest));
  }
}
