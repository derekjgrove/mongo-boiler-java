package com.mdb.ps.bo.TestCollection;

import java.util.*;

import org.bson.Document;

public class TestCollection extends Document {
    
    public TestCollection(boolean isArray, String firstLevelField, String secondLevelField, String thirdLevelField, String ssn) {

        if (isArray == true) {
            this.append("firstLevelField", firstLevelField);
            this.append("doc", new Document()
                .append("secondLevelField", secondLevelField)
                .append("borrower", new ArrayList<>(
                    (Arrays.asList(new Document()
                        .append("thirdLevelField", thirdLevelField)
                        .append("ssn", ssn)
                    ))
                ))
            );
        } else {
            this.append("firstLevelField", firstLevelField);
            this.append("doc", new Document()
                .append("secondLevelField", secondLevelField)
                .append("borrower", new Document()
                    .append("thirdLevelField", thirdLevelField)
                    .append("ssn", ssn)
                )
            );
        }
    }
    
}
