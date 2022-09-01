package com.mdb.ps.da.db;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;


public class ClusterMongoClient {
  
  private static MongoClient instance;

  private ClusterMongoClient() {}

  private static MongoClient createInstance(String uri) {
    ConnectionString connectionString = new ConnectionString(uri);

    MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString)
          .serverApi(ServerApi.builder()
              .version(ServerApiVersion.V1)
              .build())
          .build();

    return MongoClients.create(settings);
  }

  public static MongoClient getInstance(String uri) {

    if(instance == null) {
      instance = createInstance(uri);
    }

    return instance;
  }

  public static MongoClient getInstance() {
    return instance;
  }

}