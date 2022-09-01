package com.mdb.ps.bo.TestCollection;

import org.bson.Document;

public class TestCollectionSubDocSchema extends Document {

    private final String DET_ALGO = "AEAD_AES_256_CBC_HMAC_SHA_512-Deterministic";

    public TestCollectionSubDocSchema (String dekId) {
        this.append("bsonType", "object");
        this.append("properties", new Document().append(
            "ssn", new Document().append(
                "encrypt", new Document()
                    .append("bsonType", "string")
                    .append("algorithm", DET_ALGO)
            )
        ));
    }


}