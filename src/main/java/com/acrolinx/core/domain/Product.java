package com.acrolinx.core.domain;

import lombok.Getter;

import java.util.List;

@Getter
public class Product {

  private Integer id;

  private String name;

  private List<String> tags;

  private Integer quantity;
}
