package com.mdb.ps.bo.TestCollection.Explicit;

import java.util.*;

import org.bson.BsonBinary;
import org.bson.BsonString;
import org.bson.Document;

import com.mdb.ps.da.db.ExplicitCSFLEMongoClient;
import com.mdb.ps.services.kms.KMSProvider;
import com.mdb.ps.services.kms.KMSProviderSingleton;
import com.mongodb.client.model.vault.EncryptOptions;
import com.mongodb.client.vault.ClientEncryption;

public class TestCollectionExplicit extends Document {
    
    public TestCollectionExplicit(boolean isArray, String firstLevelField, String secondLevelField, String thirdLevelField, String ssn) {
        
        ClientEncryption clientEncryptionInstance = ExplicitCSFLEMongoClient.getClientEncryptionInstance();
        KMSProvider kmsProvider = KMSProviderSingleton.getInstance();

        if (isArray == true) {
            this.append("firstLevelField", firstLevelField);
            this.append("doc", new Document()
                .append("secondLevelField", secondLevelField)
                .append("borrower", new ArrayList<>(
                    (Arrays.asList(new Document()
                        .append("thirdLevelField", thirdLevelField)
                        .append("ssn", clientEncryptionInstance.encrypt(
                            new BsonString(ssn), 
                            new EncryptOptions("AEAD_AES_256_CBC_HMAC_SHA_512-Deterministic").keyId(new BsonBinary(kmsProvider.getDekUUID()))
                        ))
                    ))
                ))
            );
        } else {
            this.append("firstLevelField", firstLevelField);
            this.append("doc", new Document()
                .append("secondLevelField", secondLevelField)
                .append("borrower", new Document()
                    .append("thirdLevelField", thirdLevelField)
                    .append("ssn", clientEncryptionInstance.encrypt(
                        new BsonString(ssn), 
                        new EncryptOptions("AEAD_AES_256_CBC_HMAC_SHA_512-Deterministic").keyId(new BsonBinary(kmsProvider.getDekUUID()))
                    ))
                )
            );
        }
    }
    
}
