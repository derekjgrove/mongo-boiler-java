package com.mdb.ps;

import com.mdb.ps.bo.TestCollection.TestCollection;
import com.mdb.ps.da.dao.TestCollectionDAO;
import com.mdb.ps.da.db.CSFLEMongoClient;
import com.mdb.ps.services.kms.KMSProvider;
import com.mongodb.client.MongoClient;

/*
 * ./scripts/MakeDataKey must be run first to create your dekId and insert into meta crypto collection
 */
public class App 
{

    public static void main(String[] args) {
        System.out.println("Starting CSFLE PoC");

        App app = new App();

        //Change this value to 
        //  true = try nested array of nested docs
        //  false = try nested docs 
        boolean isArray = false;
        MockServiceStore mockServiceStore = app.new MockServiceStore(isArray);

        System.out.println(
            mockServiceStore.dal.testCollectionDAO.insert(new TestCollection(
                isArray, 
                "firstLevelField", 
                "secondLevelField", 
                "thirdLevelField", 
                "123-45-6789")
            )
        );

        System.out.println("End CSFLE PoC");

    }


    private class MockServiceStore {
        private KMSProvider kmsProvider;
        private MongoClient mongoClient;
        private DAL dal;

        public MockServiceStore(boolean isArray) {
            this.kmsProvider = new KMSProvider();
            this.mongoClient = CSFLEMongoClient.getInstance(kmsProvider, isArray);
            this.dal = new DAL(mongoClient);
        }
    }


    private class DAL {
        private TestCollectionDAO testCollectionDAO;

        DAL(MongoClient mongoClient) {
            this.testCollectionDAO = new TestCollectionDAO(mongoClient);
        }
    }
}

