package com.mdb.ps.bo.TestCollection;
import org.bson.Document;

public class TestCollectionSubArrSchema extends Document {

    public TestCollectionSubArrSchema (String dekId) {
        this.append("bsonType", "array");
        this.append("items", new TestCollectionSubDocSchema(dekId));
    }

}