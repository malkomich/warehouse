package com.acrolinx;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

public class Warehouse WidgetApplication extends Application<Warehouse WidgetConfiguration> {

    public static void main(final String[] args) throws Exception {
        new Warehouse WidgetApplication().run(args);
    }

    @Override
    public String getName() {
        return "Warehouse Widget";
    }

    @Override
    public void initialize(final Bootstrap<Warehouse WidgetConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final Warehouse WidgetConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
