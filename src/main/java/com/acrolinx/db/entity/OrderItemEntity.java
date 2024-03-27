package com.acrolinx.db.entity;

import lombok.Data;

@Data
public class OrderItemEntity {

  private String productId;

  private Integer quantity;
}