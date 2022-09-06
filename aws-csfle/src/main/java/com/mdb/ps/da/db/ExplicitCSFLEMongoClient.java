package com.mdb.ps.da.db;

import java.util.*;

import com.mdb.ps.services.kms.KMSProvider;
import com.mongodb.AutoEncryptionSettings;
import com.mongodb.ClientEncryptionSettings;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.vault.ClientEncryption;
import com.mongodb.client.vault.ClientEncryptions;


public class ExplicitCSFLEMongoClient {
  
  private static MongoClient instance;
  private static ClientEncryption clientEncryptionInstance;
  private final static String URI = "<mongo_uri>";


  private ExplicitCSFLEMongoClient() {}


  private static MongoClient createInstance(String uri, KMSProvider kmsProvider, boolean isArray) {
    ConnectionString connectionString = new ConnectionString(uri);

    Map<String, Object> extraOptions = new HashMap<String, Object>();
    extraOptions.put("mongocryptdSpawnPath", "<mongocryptd_path>");

    MongoClientSettings settings = MongoClientSettings.builder()
      .applyConnectionString(connectionString)
      .autoEncryptionSettings(AutoEncryptionSettings.builder()
        .keyVaultNamespace(kmsProvider.getKeyvaultNamespace())
        .kmsProviders(kmsProvider.getKMSProviders())
        .extraOptions(extraOptions)
        .bypassAutoEncryption(true)
        .build())
      .build();

    return MongoClients.create(settings);
  }

  private static ClientEncryption createClientEncryptionInstance(String uri, KMSProvider kmsProvider, boolean isArray) {
    ClientEncryptionSettings clientEncryptionSettings = ClientEncryptionSettings.builder()
      .keyVaultMongoClientSettings(MongoClientSettings.builder()
              .applyConnectionString(new ConnectionString(uri))
              .build())
      .keyVaultNamespace(kmsProvider.getKeyvaultNamespace())
      .kmsProviders(kmsProvider.getKMSProviders())
      .build();

    return ClientEncryptions.create(clientEncryptionSettings);
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

  public static ClientEncryption getClientEncryptionInstance(KMSProvider kmsProvider, boolean isArray) {

    if(clientEncryptionInstance == null) {
      clientEncryptionInstance = createClientEncryptionInstance(URI, kmsProvider, isArray);
    }

    return clientEncryptionInstance;
  }

  public static ClientEncryption getClientEncryptionInstance() {

    if(clientEncryptionInstance == null) {
      throw new Error("Instance was not created");
    }

    return clientEncryptionInstance;
  }

}