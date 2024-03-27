package com.acrolinx.db;

import com.acrolinx.db.entity.ProductEntity;
import com.mongodb.client.MongoCollection;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ProductMongoRepository implements ProductRepository {

  public static final String COLLECTION_NAME = "product";

  private final MongoCollection<ProductEntity> productCollection;

  @Override
  public Optional<ProductEntity> getProduct(String productId) {

    var query = new Document("_id", new ObjectId(productId));

    return Optional.ofNullable(productCollection.find(query).first());
  }

  @Override
  public List<ProductEntity> getProductsByTags(List<String> tags) {

    var query = new Document("tags", new Document("$all", tags));

    return productCollection.find(query).into(new ArrayList<>());
  }
}
