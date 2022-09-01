package com.mdb.ps.da.dao;

import com.mdb.ps.bo.TestCollection.TestCollection;
import com.mongodb.client.MongoClient;
import com.mongodb.client.result.InsertOneResult;

public class TestCollectionDAO {

    private MongoClient mongoClient;
    private final String DB_NAME = "testDB";
    private final String COLLECTION_NAME = "testCollection";

    public TestCollectionDAO(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }
    

    public InsertOneResult insert(TestCollection testCollectionBO) {
        return this.mongoClient.getDatabase(DB_NAME).getCollection(COLLECTION_NAME).insertOne(testCollectionBO); 
    }
}
