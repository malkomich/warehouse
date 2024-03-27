package com.acrolinx.db;

import com.acrolinx.db.entity.OrderEntity;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.result.InsertOneResult;
import lombok.RequiredArgsConstructor;
import org.bson.BsonObjectId;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class OrderMongoRepository implements OrderRepository {

  public static final String COLLECTION_NAME = "order";

  private final MongoCollection<OrderEntity> orderCollection;

  @Override
  public Optional<OrderEntity> getOrder(String orderId) {

    return Optional.ofNullable(
        orderCollection.find(new Document("_id", new ObjectId(orderId))).first());
  }

  @Override
  public Optional<String> createOrder(OrderEntity order) {

    return Optional.of(orderCollection.insertOne(order))
        .map(InsertOneResult::getInsertedId)
        .map(BsonValue::asObjectId)
        .map(BsonObjectId::getValue)
        .map(ObjectId::toString);
  }

  @Override
  public Optional<OrderEntity> replace(OrderEntity order) {

    var options = new FindOneAndReplaceOptions()
        .returnDocument(com.mongodb.client.model.ReturnDocument.AFTER);

    var orderEntity = orderCollection.findOneAndReplace(
        new Document("_id", order.getId()),
        order,
        options);

    return Optional.ofNullable(orderEntity);
  }

  @Override
  public Optional<OrderEntity> update(OrderEntity order) {

    var update = new Document();

    if (Objects.nonNull(order.getShipDate())) {
      update.append("shipDate", order.getShipDate());
    }

    if (Objects.nonNull(order.getStatus())) {
      update.append("status", order.getStatus());
    }

    var options = new FindOneAndUpdateOptions()
        .returnDocument(com.mongodb.client.model.ReturnDocument.AFTER);

    var orderEntity = orderCollection.findOneAndUpdate(
        new Document("_id", order.getId()),
        new Document("$set", update),
        options);

    return Optional.ofNullable(orderEntity);
  }

  @Override
  public Optional<OrderEntity> delete(String orderId) {

    return Optional.ofNullable(
        orderCollection.findOneAndDelete(new Document("_id", new ObjectId(orderId))));
  }
}
