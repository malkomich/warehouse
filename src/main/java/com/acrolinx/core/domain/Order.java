package com.acrolinx.core.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class Order {

  private Integer id;

  private List<OrderItem> orderItems;

  private LocalDate shipDate;

  private Status status;

  public enum Status {
    PLACED,
    APPROVED,
    DELIVERED
  }
}
