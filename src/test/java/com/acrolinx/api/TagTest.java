package com.acrolinx.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TagTest {

  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

  @Test
  void seralizesToJSON() throws Exception {

    var tag = new Tag("livingroom");

    final String expected = MAPPER.writeValueAsString(
        MAPPER.readValue(getClass().getResource("/fixtures/api/tag.json"), OrderItem.class));

    Assertions.assertEquals(expected, MAPPER.writeValueAsString(tag));
  }
}
