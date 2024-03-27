package com.acrolinx.db;

import com.acrolinx.config.MongoConfiguration;
import com.acrolinx.config.MongoFactory;
import com.mongodb.client.MongoDatabase;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.MountableFile;

public class TestDbContainer extends GenericContainer<TestDbContainer> {

  private static final Integer PORT = 27017;
  private static final String RESOURCE_PATH = "db/init-data.js";
  private static final String DATABASE_NAME = "test";

  public TestDbContainer() {
    super("mongo:latest");

    withExposedPorts(PORT);
    withCopyFileToContainer(
        MountableFile.forClasspathResource(RESOURCE_PATH),
        "/docker-entrypoint-initdb.d/init-data.js");
  }

  public MongoDatabase getDatabase() {

    if (!isRunning()) {
      throw new IllegalStateException("Container should be running to get the database instance");
    }

    var connectionString = "mongodb://" + getHost() + ":" + getFirstMappedPort();
    var mongoConfiguration = new MongoConfiguration();
    mongoConfiguration.setUri(connectionString);

    return new MongoFactory(mongoConfiguration)
        .getClient()
        .getDatabase(DATABASE_NAME);
  }
}
