package com.acrolinx.api;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem implements Serializable {

  @Min(1)
  private Integer productId;

  @Min(1)
  private Integer quantity;
}