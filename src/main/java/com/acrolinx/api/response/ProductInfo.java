package com.acrolinx.api.response;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "ProductInfo")
public class ProductInfo {

  private Integer id;

  private String name;

  private List<String> tags;

  private Integer quantity;
}
