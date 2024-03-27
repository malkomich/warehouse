package com.acrolinx.db.entity;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

@Data
public class ProductEntity {

  private ObjectId id;

  private String name;

  private List<String> tags;

  private Integer quantity;

  public String getIdString() {
    return Optional.ofNullable(id)
        .map(ObjectId::toString)
        .orElse(null);
  }
}
