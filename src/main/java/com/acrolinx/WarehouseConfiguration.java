package com.acrolinx;

import com.acrolinx.config.MongoConfiguration;
import com.acrolinx.config.RedisConfiguration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;
import lombok.Getter;

public class WarehouseConfiguration extends Configuration {

  @Getter
  @JsonProperty("mongodb")
  private MongoConfiguration mongoConfiguration;

  @Getter
  @JsonProperty("redis")
  private RedisConfiguration redisConfiguration;
}
