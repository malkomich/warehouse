package com.acrolinx.api.request;

import com.acrolinx.api.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderPartialUpdateRequestTest {

  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

  @Test
  void seralizesToJSON() throws Exception {

    var orderRequest = new OrderPartialUpdateRequest(
        "2019-02-03",
        Status.APPROVED);

    final String expected = MAPPER.writeValueAsString(
        MAPPER.readValue(
            getClass().getResource("/fixtures/api/orderPartialUpdateRequest.json"),
            OrderPartialUpdateRequest.class));

    Assertions.assertEquals(expected, MAPPER.writeValueAsString(orderRequest));
  }
}
