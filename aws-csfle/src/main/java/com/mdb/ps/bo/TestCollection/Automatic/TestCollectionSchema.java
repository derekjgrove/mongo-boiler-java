package com.mdb.ps.bo.TestCollection.Automatic;
import java.util.*;
import org.bson.Document;

public class TestCollectionSchema extends Document {

    public TestCollectionSchema (String dekId, boolean isArray) {
        this.append("bsonType", "object");
        this.append("encryptMetadata", new Document().append(
                "keyId", new ArrayList<>((Arrays.asList(new Document().append("$binary", new Document().append("base64", dekId).append("subType", "04")))))
        ));

        this.append("properties", new Document()
            .append("doc", new Document()
                .append("bsonType", "object")
                .append("properties", new Document()
                    .append("borrower", isArray == true ? new TestCollectionSubArrSchema() : new TestCollectionSubDocSchema())
                )
            )
        );

    }

    public TestCollectionSchema (UUID dekId, boolean isArray) {
        this.append("bsonType", "object");
        this.append("encryptMetadata", new Document().append(
                "keyId", new ArrayList<>((Arrays.asList(dekId)))
        ));

        this.append("properties", new Document()
            .append("doc", new Document()
                .append("bsonType", "object")
                .append("properties", new Document()
                    .append("borrower", isArray == true ? new TestCollectionSubArrSchema() : new TestCollectionSubDocSchema())
                )
            )
        );

    }

}