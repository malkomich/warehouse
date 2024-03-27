# Warehouse Widget

Getting started
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/warehouse-1.0.0-SNAPSHOT.jar server target/config.yml`
1. To check that your application is running locally enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

Database
---

The application uses MongoDB database. The library `org.testcontainers:mongodb` has been used to build integration test
in the persistence layer.

Use Cases
---
- Shop wants to check stock of a product on the warehouse **[GET]** _#product_
- Shop wants to request a restocking of a product from the warehouse **[POST]** _#order_
- Shop wants to cancel request of restocking a product **[DELETE]** _#order_
- Shop wants to check the status of an ongoing order **[GET]** _#order_
- Shop wants to completely update an ongoing order **[UPDATE]** _#order_
- Warehouse updates the order status when it is shipped, and shop update it when it is delivered **[PATCH]** _#order_
- Shop wants to filter the available products by different tags **[GET]** _#product_
