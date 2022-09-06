package com.mdb.ps.scripts;

import java.util.HashMap;
import java.util.Map;
import java.util.Base64;

import org.bson.BsonBinary;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.BsonBoolean;
import org.bson.BsonInt32;

import com.mongodb.ClientEncryptionSettings;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.model.vault.DataKeyOptions;
import com.mongodb.client.vault.ClientEncryption;
import com.mongodb.client.vault.ClientEncryptions;
import com.mongodb.client.model.IndexOptions;


/*
 * - Reads master key from file "master-key.txt" in root directory of project, or creates one on a KMS
 * - Locates existing local encryption key from encryption.__keyVault collection, or from a KMS
 * - Prints base 64-encoded value of the data encryption key
 */
public class MakeDataKey {

    public static void main(String[] args) throws Exception {

        // start-kmsproviders
        Map<String, Map<String, Object>> kmsProviders = new HashMap<String, Map<String, Object>>();
        String kmsProvider = "aws";
        Map<String, Object> providerDetails = new HashMap<>();
        providerDetails.put("accessKeyId", new BsonString("<ACCESS_KEY_ID>"));
        providerDetails.put("secretAccessKey", new BsonString("<SECRET_ACCESS_KEY>"));
        providerDetails.put("sessionToken", new BsonString("<SESSION_TOKEN>"));
        kmsProviders.put(kmsProvider, providerDetails);
        // end-kmsproviders

        // start-datakeyopts
        BsonDocument masterKeyProperties = new BsonDocument();
        // HashMap<String, BsonDocument> masterKeyProperties = new HashMap<String, BsonDocument>();
        masterKeyProperties.put("provider", new BsonString(kmsProvider));
        masterKeyProperties.put("key", new BsonString("<key_arn>"));
        masterKeyProperties.put("region", new BsonString("<key_region>"));
        // end-datakeyopts

        // start-create-index
        String connectionString = "<mongo_uri>";
        String keyVaultDb = "__encryption";
        String keyVaultColl = "__keyVault";
        String keyVaultNamespace = keyVaultDb + "." + keyVaultColl;
        MongoClient keyVaultClient = MongoClients.create(connectionString);


        keyVaultClient.getDatabase(keyVaultDb).getCollection(keyVaultColl).drop();

        MongoCollection keyVaultCollection = keyVaultClient.getDatabase(keyVaultDb).getCollection(keyVaultColl);
        IndexOptions indexOpts = new IndexOptions().partialFilterExpression(new BsonDocument("keyAltNames", new BsonDocument("$exists", new BsonBoolean(true) ))).unique(true);
        keyVaultCollection.createIndex(new BsonDocument("keyAltNames", new BsonInt32(1)), indexOpts);
        keyVaultClient.close();
        // end-create-index 

        // start-create-dek
        ClientEncryptionSettings clientEncryptionSettings = ClientEncryptionSettings.builder()
                .keyVaultMongoClientSettings(MongoClientSettings.builder()
                        .applyConnectionString(new ConnectionString(connectionString))
                        .build())
                .keyVaultNamespace(keyVaultNamespace)
                .kmsProviders(kmsProviders)
                .build();
        
        ClientEncryption clientEncryption = ClientEncryptions.create(clientEncryptionSettings);
        BsonBinary dataKeyId = clientEncryption.createDataKey(kmsProvider, new DataKeyOptions().masterKey(masterKeyProperties));
        String base64DataKeyId = Base64.getEncoder().encodeToString(dataKeyId.getData());
        System.out.println("DataKeyId [base64]: " + base64DataKeyId);
        clientEncryption.close();
        // end-create-dek
    }
}