package com.acrolinx.api.request;

import com.acrolinx.api.OrderItem;
import com.acrolinx.api.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class OrderUpdateRequestTest {

  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

  @Test
  void seralizesToJSON() throws Exception {

    var orderRequest = new OrderUpdateRequest(
        "2019-02-03",
        Collections.singletonList(new OrderItem("abc123abc123abc123abc123", 20)),
        Status.APPROVED);

    final String expected = MAPPER.writeValueAsString(
        MAPPER.readValue(
            getClass().getResource("/fixtures/api/orderUpdateRequest.json"),
            OrderUpdateRequest.class));

    Assertions.assertEquals(expected, MAPPER.writeValueAsString(orderRequest));
  }
}
