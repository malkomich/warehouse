db.product.insertMany([
  {
    "_id": ObjectId("617d673dd59fd40c61b1b371"),
    "name": "Fabric chaise lounge grey sofa",
    "tags": [
      "grey",
      "fabric",
      "sofa",
      "livingroom"
    ],
    "quantity": 10
  }
])

db.order.insertMany([
  {
    "_id": ObjectId("617d673dd59fd40c61b1b370"),
    "orderItems": [
      {
        "productId": "223abc123abc123abc123abc",
        "quantity": 2
      },
      {
        "productId": "123abc123abc123abc123abc",
        "quantity": 1
      }
    ],
    "shipDate": "2024-03-29",
    "status": "PLACED"
  }
])
