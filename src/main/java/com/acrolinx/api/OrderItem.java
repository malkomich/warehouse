package com.acrolinx.api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class OrderItem implements Serializable {

  private Integer productId;

  private Integer quantity;
}