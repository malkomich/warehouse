package com.acrolinx.core;

import com.acrolinx.core.domain.Product;

import java.util.Optional;

public interface GetProductUseCase {

  Optional<Product> getProductById(final String productId);
}
