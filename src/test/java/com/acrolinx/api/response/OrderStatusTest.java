package com.acrolinx.api.response;

import com.acrolinx.api.OrderItem;
import com.acrolinx.api.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

class OrderStatusTest {

  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

  @Test
  void seralizesToJSON() throws Exception {

    var orderItem = new OrderItem("123abc123abc123abc123abc", 10);
    var orderStatus = new OrderStatus("123abc123abc123abc123abc",
        Collections.singletonList(orderItem),
        LocalDate.parse("2019-02-03", DATE_FORMATTER),
        Status.APPROVED);

    final String expected = MAPPER.writeValueAsString(
        MAPPER.readValue(getClass().getResource("/fixtures/api/orderStatus.json"), OrderStatus.class));

    Assertions.assertEquals(expected, MAPPER.writeValueAsString(orderStatus));
  }
}
