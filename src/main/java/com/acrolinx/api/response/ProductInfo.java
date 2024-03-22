package com.acrolinx.api.response;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@XmlRootElement(name = "ProductInfo")
public class ProductInfo {

  private Integer id;

  private String name;

  private List<Tag> tags;

  private Integer quantity;

  class Tag {

    private String name;
  }
}