package com.acrolinx.core.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItem {

  private String productId;

  private Integer quantity;
}
