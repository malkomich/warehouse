package com.acrolinx.core;

import com.acrolinx.core.domain.Product;
import com.acrolinx.db.ProductRepository;
import com.acrolinx.db.entity.ProductEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ProductService implements GetProductUseCase, FilterProductsUseCase {

  private final ProductRepository productRepository;

  @Override
  public Optional<Product> getProductById(String productId) {

    return productRepository.getProduct(productId)
        .map(this::toDomain);
  }

  @Override
  public List<Product> filterProductsByTags(List<String> tags) {

    return productRepository.getProductsByTags(tags)
        .stream()
        .map(this::toDomain)
        .collect(Collectors.toList());
  }

  private Product toDomain(ProductEntity productEntity) {

    return Product.builder()
        .id(productEntity.getIdString())
        .name(productEntity.getName())
        .tags(productEntity.getTags())
        .quantity(productEntity.getQuantity())
        .build();
  }
}
