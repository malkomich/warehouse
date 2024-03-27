package com.acrolinx.core.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
@Builder
public class Order {

  private String id;

  private List<OrderItem> orderItems;

  private LocalDate shipDate;

  private Status status;

  public String getStatusName() {
    return Optional.ofNullable(status)
        .map(Enum::name)
        .orElse(null);
  }

  public enum Status {
    PLACED,
    APPROVED,
    DELIVERED;

    public static Status from(String value) {
      return Stream.of(values())
          .filter(status -> status.name().equals(value))
          .findFirst()
          .orElse(null);
    }
  }
}
