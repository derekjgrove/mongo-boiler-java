package com.mdb.ps;

import com.mdb.ps.bo.TestCollection.Automatic.TestCollection;
import com.mdb.ps.bo.TestCollection.Explicit.TestCollectionExplicit;
import com.mdb.ps.da.dao.TestCollectionDAO;
import com.mdb.ps.da.db.AutomaticCSFLEMongoClient;
import com.mdb.ps.da.db.ExplicitCSFLEMongoClient;
import com.mdb.ps.services.kms.KMSProvider;
import com.mdb.ps.services.kms.KMSProviderSingleton;
import com.mongodb.client.MongoClient;
import com.mongodb.client.vault.ClientEncryption;

/*
 * ./scripts/MakeDataKey must be run first to create your dekId and insert into meta crypto collection
 */
public class App {

    public static void main(String[] args) {
        System.out.println("Starting CSFLE PoC");

        App app = new App();

        //Change this value to 
        //  true = try nested array of nested docs
        //  false = try nested docs 
        boolean isArray = false;
        boolean isExplicitEncryption = true;
        MockServiceStore mockServiceStore = app.new MockServiceStore(isArray, isExplicitEncryption);

        if (isExplicitEncryption == true) {
            System.out.println(
                mockServiceStore.dal.testCollectionDAO.insertExplicitCSFLE(new TestCollectionExplicit(
                    isArray,
                    "firstLevelField", 
                    "secondLevelField", 
                    "thirdLevelField", 
                    "123-45-6789")
                )
            );
        } else {
            System.out.println(
                mockServiceStore.dal.testCollectionDAO.insert(new TestCollection(
                    isArray,
                    "firstLevelField", 
                    "secondLevelField", 
                    "thirdLevelField", 
                    "123-45-6789")
                )
            );
        }

        System.out.println("End CSFLE PoC");

    }


    private class MockServiceStore {
        private KMSProvider kmsProvider;
        private MongoClient mongoClient;
        private ClientEncryption clientEncryptionInstance;
        private DAL dal;

        public MockServiceStore(boolean isArray, boolean isExplicitEncryption) {
            
            this.kmsProvider = KMSProviderSingleton.getInstance();

            if (isExplicitEncryption == true) {
                this.mongoClient = ExplicitCSFLEMongoClient.getInstance(kmsProvider, isArray);
                this.clientEncryptionInstance = ExplicitCSFLEMongoClient.getClientEncryptionInstance(kmsProvider, isArray);
                this.dal = new DAL(mongoClient, clientEncryptionInstance);
            } else {
                this.mongoClient = AutomaticCSFLEMongoClient.getInstance(kmsProvider, isArray);
                this.dal = new DAL(mongoClient);
            }
        }
    }


    private class DAL {
        private TestCollectionDAO testCollectionDAO;
        private ClientEncryption clientEncryptionInstance;

        DAL(MongoClient mongoClient) {
            this.testCollectionDAO = new TestCollectionDAO(mongoClient);
        }

        DAL(MongoClient mongoClient, ClientEncryption clientEncryptionInstance) {
            this.clientEncryptionInstance = clientEncryptionInstance;
            this.testCollectionDAO = new TestCollectionDAO(mongoClient, clientEncryptionInstance);
        }
    }
}

