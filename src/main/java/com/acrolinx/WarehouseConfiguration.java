package com.acrolinx;

import com.acrolinx.config.MongoConfiguration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;

public class WarehouseConfiguration extends Configuration {

  @JsonProperty("mongodb")
  private MongoConfiguration mongoConfiguration;

  public MongoConfiguration getMongoConfiguration() {
    return mongoConfiguration;
  }
}
