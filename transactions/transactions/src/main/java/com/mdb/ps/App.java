package com.mdb.ps;

import org.bson.Document;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.TransactionBody;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;


public class App {

    /*
     * Logic to compare the original and newly synced document and return the delta
     * 
     * Note: Implemented logic is not actual solution
     */
    public static Document getDelta(Document original, Document sync) {
        return (Document) sync.get("talentProfile");
    }


    public static void main(String[] args) {

        /* Connection Details - this would be a singleton */
        String connectionString = "<URI>";
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();

        final MongoClient mongoClient = MongoClients.create(settings);


        /* Session and transaction initialization to use a MongoDB Transaction */
        final ClientSession clientSession = mongoClient.startSession();

        TransactionOptions txnOptions = TransactionOptions.builder()
                .readPreference(ReadPreference.primary())
                .readConcern(ReadConcern.LOCAL)
                .writeConcern(WriteConcern.MAJORITY)
                .build();


        /*
         * Transaction Logic
         * 1. [FIND] Iterate through staging documents that are {status: "validated"}
         * 2. Delete any non-transferrable fields from staging document and apply any transformation
         * 3. [REPLACE] Replace the master document with the ready sync document and return Master original
         * 4. Prepare the local previous Master version with any processing and meta details
         *      and insert into versions collections
         * 5. [UPDATE] Update all related versions for online archive purposes
         * 6. [DELETE] Delete the sync document from the sync collection
         * 
         * Collections impacted:
         * - sync
         * - master
         * - versions
        */
        TransactionBody txnBody = new TransactionBody<String>() {

            public String execute() {

                System.out.println("---------- Starting Transaction ----------");
                
                MongoDatabase db = mongoClient.getDatabase("hcm");

                // Find staging documents ready to be persisted to master
                FindIterable<Document> stagingResultSet = db.getCollection("master_staging").find(new Document("status", "validated"));
                MongoCursor<Document> cursor = stagingResultSet.iterator();

                try {
                    
                    while (cursor.hasNext()) {
                        
                        Document stagingDocument = cursor.next();

                        System.out.println("Syncing Data for " + ((Document) stagingDocument.get("key")).get("personNumber"));

                        // Delete any non-transferrable fields
                        ObjectId removalId = (ObjectId) stagingDocument.get("_id");
                        stagingDocument.remove("status");
                        stagingDocument.remove("_id");

                        // ... any other transformation on sync document

                        // Replace the master document with the ready-state sync document and return the original
                        FindOneAndReplaceOptions opts = new FindOneAndReplaceOptions();
                        opts.upsert(true);
                        opts.returnDocument(ReturnDocument.BEFORE);

                        Document existingMasterDocument = db.getCollection("master_current").findOneAndReplace(
                            clientSession, 
                            new Document("key.personNumber", ((Document) stagingDocument.get("key")).get("personNumber")), 
                            stagingDocument, 
                            opts
                        );

                        // Apply any operational/processing and other meta details to version document and insert
                        Document delta = getDelta(existingMasterDocument, stagingDocument);
                        existingMasterDocument.append("_meta", new Document("delta", delta));

                        db.getCollection("master_versions").insertOne(clientSession, existingMasterDocument);

                        // Update the _onlineArchiveVersion for the OA rule
                        db.getCollection("master_versions").updateMany(
                            clientSession, 
                            new Document("key.personNumber", ((Document) stagingDocument.get("key")).get("personNumber")), 
                            new Document("$inc", (new Document("_onlineArchiveVersion", 1)))
                        );

                        db.getCollection("master_staging").deleteOne(new Document("_id", removalId));

                    }
                } finally {
                    cursor.close();
                }

                System.out.println("---------- Ending Transaction ----------");
                return "Sync from Staging to Master complete";
            }
        };


        try {
            /*
               Step 4: Use .withTransaction() to start a transaction,
               execute the callback, and commit (or abort on error).
            */
            clientSession.withTransaction(txnBody, txnOptions);
        } catch (RuntimeException e) {
            // some error handling
        } finally {
            clientSession.close();
        }
    }
}
