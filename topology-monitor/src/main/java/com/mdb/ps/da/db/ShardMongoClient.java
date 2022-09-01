package com.mdb.ps.da.db;

import java.util.ArrayList;
import java.util.List;

import com.mdb.ps.da.listeners.ServerListenerRegion;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;


public class ShardMongoClient {
  
  private static List<MongoClient> instances;

  private ShardMongoClient() {}

  private static MongoClient createInstance(String uri) {
    ConnectionString connectionString = new ConnectionString(uri);

    MongoClientSettings settings = MongoClientSettings.builder()
          .applyConnectionString(connectionString)
          .serverApi(ServerApi.builder()
              .version(ServerApiVersion.V1)
              .build())
          .applyToServerSettings(builder ->
              builder.addServerListener(new ServerListenerRegion()))
          .build();

    return MongoClients.create(settings);
  }

  private static List<MongoClient> createInstances(List<String> uris) {
    
    List<MongoClient> mongoClients = new ArrayList<MongoClient>();

    for (String uri : uris) {
      mongoClients.add(createInstance(uri));
    }
    
    return mongoClients;

  }

  public static List<MongoClient> getInstance(List<String> uris) {

    if(instances == null) {
      instances = createInstances(uris);
    }

    return instances;
  }

  public static List<MongoClient> getInstance() {
    return instances;
  }

}