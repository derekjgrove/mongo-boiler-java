package com.mdb.ps.da.dao;

import com.mdb.ps.bo.TestCollection.Automatic.TestCollection;
import com.mdb.ps.bo.TestCollection.Explicit.TestCollectionExplicit;
import com.mongodb.client.MongoClient;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.vault.ClientEncryption;

public class TestCollectionDAO {

    private MongoClient mongoClient;
    private ClientEncryption clientEncryptionInstance;
    private final String DB_NAME = "testDB";
    private final String COLLECTION_NAME = "testCollection";

    public TestCollectionDAO(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public TestCollectionDAO(MongoClient mongoClient, ClientEncryption clientEncryptionInstance) {
        this.mongoClient = mongoClient;
        this.clientEncryptionInstance = clientEncryptionInstance;
    }

    public InsertOneResult insert(TestCollection testCollectionBO) {
        return this.mongoClient.getDatabase(DB_NAME).getCollection(COLLECTION_NAME).insertOne(testCollectionBO); 
    }

    public InsertOneResult insertExplicitCSFLE(TestCollectionExplicit testCollectionBO) {
        return this.mongoClient.getDatabase(DB_NAME).getCollection(COLLECTION_NAME).insertOne(testCollectionBO); 
    }
}
