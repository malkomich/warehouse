package com.acrolinx.core.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class Product {

  private String id;

  private String name;

  private List<String> tags;

  private Integer quantity;
}
