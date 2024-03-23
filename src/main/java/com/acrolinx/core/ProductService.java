package com.acrolinx.core;

import com.acrolinx.core.domain.Product;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ProductService implements GetProductUseCase, FilterProductsUseCase {

  @Override
  public Optional<Product> getProductById(Integer productId) {
    return Optional.empty();
  }

  @Override
  public List<Product> filterProductsByTags(List<String> tags) {
    return Collections.emptyList();
  }
}
