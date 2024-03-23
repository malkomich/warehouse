package com.acrolinx.core;

import com.acrolinx.core.domain.Product;

import java.util.List;

public interface FilterProductsUseCase {

  List<Product> filterProductsByTags(final List<String> tags);
}
