package com.acrolinx.resources.product;

import com.acrolinx.api.response.ProductInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProductInfoCacheSerializer {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static String serialize(ProductInfo productInfo) {
    try {
      return objectMapper.writeValueAsString(productInfo);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public static ProductInfo deserialize(String json) {
    try {
      return objectMapper.readValue(json, ProductInfo.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
