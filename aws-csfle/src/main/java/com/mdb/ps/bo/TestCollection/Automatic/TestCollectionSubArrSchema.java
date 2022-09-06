package com.mdb.ps.bo.TestCollection.Automatic;

import org.bson.Document;

/*
 * After testing, Automatic CSFLE does not support array of elements and resorting to 
 * explicit encryption is required.
 * 
 * 
 */
public class TestCollectionSubArrSchema extends Document {

    public TestCollectionSubArrSchema () {
        this.append("bsonType", "array");
        this.append("items", new TestCollectionSubDocSchema());
    }

}