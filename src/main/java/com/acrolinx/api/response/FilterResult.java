package com.acrolinx.api.response;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@XmlRootElement(name = "FilterResult")
public class FilterResult {

  private List<ProductInfo> products;
}
