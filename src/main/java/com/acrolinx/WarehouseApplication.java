package com.acrolinx;

import com.acrolinx.core.OrderService;
import com.acrolinx.core.ProductService;
import com.acrolinx.resources.order.OrderResource;
import com.acrolinx.resources.product.ProductResource;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
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
        // TODO: application initialization
    }

    @Override
    public void run(final WarehouseConfiguration configuration,
                    final Environment environment) {

        final ProductService productService = new ProductService();
        final OrderService orderService = new OrderService();

        environment.jersey().register(new ProductResource(productService, productService));
        environment.jersey().register(new OrderResource(orderService));

        environment.jersey().register(new OpenApiResource()
            .configLocation("/openapi.yaml"));
    }

}
