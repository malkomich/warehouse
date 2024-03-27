package com.acrolinx.resources.product;

import com.acrolinx.api.response.ProductInfo;
import com.acrolinx.core.domain.Product;

public class ProductMapper {

  private ProductMapper() {}

  static ProductInfo toProductInfo(Product product) {
    return new ProductInfo(product.getId(), product.getName(), product.getTags(), product.getQuantity());
  }
}
