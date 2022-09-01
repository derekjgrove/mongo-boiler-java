package com.mdb.ps.da.dao;

import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoClient;

public class AdminDAO {

    private MongoClient mongoClient;

    public AdminDAO(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }
    
    public BsonDocument listShards() {
        Bson command = new BsonDocument("listShards", new BsonInt64(1));
        Document commandResult = mongoClient.getDatabase("admin").runCommand(command);

        return commandResult.toBsonDocument();
    }
}
