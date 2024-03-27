package com.acrolinx.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem implements Serializable {

  @Size(min = 24, max = 24, message = "Product id format is invalid")
  private String productId;

  @Min(1)
  private Integer quantity;
}