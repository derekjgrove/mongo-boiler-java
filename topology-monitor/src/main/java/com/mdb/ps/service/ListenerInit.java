package com.mdb.ps.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;

import com.mdb.ps.da.dao.AdminDAO;
import com.mdb.ps.da.db.ClusterMongoClient;
import com.mdb.ps.da.db.ShardMongoClient;
import com.mongodb.client.MongoClient;

public class ListenerInit {

    private static final String CLUSTER_URI = "mongodb+srv://<user>:<pass>@<srv>/?";
    private MongoClient shardedClusterClient;
    private List<MongoClient> shardClients = new ArrayList<MongoClient>();

    public void initializeListeners() {

        shardedClusterClient = ClusterMongoClient.getInstance(CLUSTER_URI + 
            "retryWrites=true&" + 
            "heartbeatFrequencyMS=1000");

        AdminDAO adminDAO = new AdminDAO(shardedClusterClient);
        List<String> uris = getRsURIs(adminDAO.listShards());
        

        shardClients = ShardMongoClient.getInstance(uris);

    }

    private List<String> getRsURIs(BsonDocument shards) {
        List<String> uris = new ArrayList<String>();

        for (BsonValue shard : (BsonArray) shards.get("shards")) {
            String host = shard.asDocument().get("host").asString().getValue();

            String [] fields = ((String) host).split("/");

            uris.add("mongodb://<user>:<pass>@" + fields[1] + "/?" +
                "tls=true&" +
                "replicaSet=" + fields[0] + "&" +
                "readPreference=primaryPreferred&" +
                "heartbeatFrequencyMS=1000");
        }

        return uris;
    }
}


