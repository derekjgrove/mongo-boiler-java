package com.mdb.ps.da.db;

import java.util.*;

import org.bson.BsonDocument;

import com.mdb.ps.bo.TestCollection.Automatic.TestCollectionSchema;
import com.mdb.ps.services.kms.KMSProvider;
import com.mongodb.AutoEncryptionSettings;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;


public class AutomaticCSFLEMongoClient {
  
  private static MongoClient instance;
  private final static String URI = "<mongo_uri>";


  private AutomaticCSFLEMongoClient() {}


  private static HashMap<String, BsonDocument> combineSchemas(String dekId, boolean isArray) {
    HashMap<String, BsonDocument> schemaMap = new HashMap<String, BsonDocument>();

    schemaMap.put("testDB.testCollection", BsonDocument.parse(new TestCollectionSchema(dekId, isArray).toJson()));

    System.out.println(schemaMap.toString());
    return schemaMap;
  }

  private static HashMap<String, BsonDocument> combineSchemas(UUID dekId, boolean isArray) {
    HashMap<String, BsonDocument> schemaMap = new HashMap<String, BsonDocument>();

    schemaMap.put("testDB.testCollection", BsonDocument.parse(new TestCollectionSchema(dekId, isArray).toJson()));

    System.out.println(schemaMap.toString());
    return schemaMap;
  }


  private static MongoClient createInstance(String uri, KMSProvider kmsProvider, boolean isArray) {
    ConnectionString connectionString = new ConnectionString(uri);

    Map<String, Object> extraOptions = new HashMap<String, Object>();
    extraOptions.put("mongocryptdSpawnPath", "<mongocryptd_path>");

    MongoClientSettings settings = MongoClientSettings.builder()
      .applyConnectionString(connectionString)
      .autoEncryptionSettings(AutoEncryptionSettings.builder()
        .keyVaultNamespace(kmsProvider.getKeyvaultNamespace())
        .kmsProviders(kmsProvider.getKMSProviders())
        .schemaMap(combineSchemas(kmsProvider.getDekUUID(), isArray))
        .extraOptions(extraOptions)
        .build())
      .build();

    return MongoClients.create(settings);
  }

  public static MongoClient getInstance(String uri, KMSProvider kmsProvider, boolean isArray) {

    if(instance == null) {
      instance = createInstance(uri, kmsProvider, isArray);
    }

    return instance;
  }

  public static MongoClient getInstance(KMSProvider kmsProvider, boolean isArray) {

    if(instance == null) {
      instance = createInstance(URI, kmsProvider, isArray);
    }

    return instance;
  }

}