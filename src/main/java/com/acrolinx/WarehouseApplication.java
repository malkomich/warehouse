package com.acrolinx;

import com.acrolinx.api.response.ProductInfo;
import com.acrolinx.config.MongoFactory;
import com.acrolinx.core.OrderService;
import com.acrolinx.core.ProductService;
import com.acrolinx.db.OrderMongoRepository;
import com.acrolinx.db.ProductMongoRepository;
import com.acrolinx.db.entity.OrderEntity;
import com.acrolinx.db.entity.ProductEntity;
import com.acrolinx.resources.order.OrderResource;
import com.acrolinx.resources.product.ProductResource;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = WarehouseApplication.APPLICATION_NAME,
        version = "1.0.0-SNAPSHOT",
        description = "Warehouse service responsible for managing product stock and examining its availability",
        contact = @Contact(name = "Juan Carlos Gonzalez Cabrero", email = "juancarlos.gonzalez.cabrero@gmail.com")
    )
)
public class WarehouseApplication extends Application<WarehouseConfiguration> {

    static final String APPLICATION_NAME = "Warehouse Widget";

    public static void main(final String[] args) throws Exception {
        new WarehouseApplication().run(args);
    }

    @Override
    public String getName() {
        return APPLICATION_NAME;
    }

    @Override
    public void initialize(final Bootstrap<WarehouseConfiguration> bootstrap) {

        bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());
    }

    @Override
    public void run(final WarehouseConfiguration configuration,
                    final Environment environment) {

        var mongoDatabase = new MongoFactory(configuration.getMongoConfiguration())
            .getClient()
            .getDatabase(configuration.getMongoConfiguration().getDatabase());

        var redisConfig = configuration.getRedisConfiguration();
        var redisClient = RedisClient.create("redis://" + redisConfig.getHost() + ":" + redisConfig.getPort());
        var redisConnection = redisClient.connect();

        var orderRepository = new OrderMongoRepository(
            mongoDatabase.getCollection(OrderMongoRepository.COLLECTION_NAME, OrderEntity.class));
        var productRepository = new ProductMongoRepository(
            mongoDatabase.getCollection(ProductMongoRepository.COLLECTION_NAME, ProductEntity.class));
        var productService = new ProductService(productRepository);
        var orderService = new OrderService(orderRepository);

        environment.jersey().register(new ProductResource(productService, productService, redisConnection));
        environment.jersey().register(new OrderResource(orderService));
    }
}
