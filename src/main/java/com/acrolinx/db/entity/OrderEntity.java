package com.acrolinx.db.entity;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

@Data
public class OrderEntity {

  private ObjectId id;

  private List<OrderItemEntity> orderItems;

  private String shipDate;

  private String status;

  public String getIdString() {
    return Optional.ofNullable(id)
        .map(ObjectId::toString)
        .orElse(null);
  }
}
