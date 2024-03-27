package com.acrolinx.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.RequiredArgsConstructor;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

@RequiredArgsConstructor
public class MongoFactory {

  private final MongoConfiguration mongoConfiguration;

  public MongoClient getClient() {

    var settings = MongoClientSettings.builder()
        .applyConnectionString(new ConnectionString(mongoConfiguration.getUri()))
        .codecRegistry(codecRegistry())
        .serverApi(serverApi())
        .build();

    return MongoClients.create(settings);
  }

  private static CodecRegistry codecRegistry() {

    return CodecRegistries.fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder().automatic(true).build()));
  }

  private ServerApi serverApi() {

    return ServerApi.builder()
        .version(ServerApiVersion.V1)
        .build();
  }
}
