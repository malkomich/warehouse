package com.acrolinx.db;

import com.acrolinx.db.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

  Optional<ProductEntity> getProduct(String productId);

  List<ProductEntity> getProductsByTags(List<String> tags);

}
