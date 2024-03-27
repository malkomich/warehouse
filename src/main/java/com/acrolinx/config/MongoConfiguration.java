package com.acrolinx.config;

import lombok.Data;

@Data
public class MongoConfiguration {

  private String uri;

  private String database;

}
