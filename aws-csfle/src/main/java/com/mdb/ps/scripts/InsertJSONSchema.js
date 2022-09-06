/*

 To run this file, utilize mongosh with load function - https://www.mongodb.com/docs/manual/reference/method/load/


 mongosh <uri> <other_options> 
 load("aws-csfle/src/main/java/com/mdb/ps/scripts/InsertJSONSchema.js")


 Creates the $jsonSchema for testDB.testCollection
 
*/ 

db = db.getSiblingDB("testDB");

db.runCommand({
	collMod: "testCollection",
	validator: {
	  $jsonSchema: {
		bsonType: 'object',
		encryptMetadata: {
		  "keyId": [UUID("<DEK_UUID>")]
		},
		properties: {
		  doc: {
			bsonType: 'object',
			properties: {
			  borrower: {
				"anyOf":[
				  {
					bsonType: 'object',
					properties: {
					  ssn: {
						encrypt: {
						  bsonType: 'string',
						  algorithm: 'AEAD_AES_256_CBC_HMAC_SHA_512-Deterministic'
						}
					  }
					}
				  },
				  {
					bsonType: 'array',
					items: {
					  bsonType: 'object',
					  properties: {
						ssn: {
						  encrypt: {
							bsonType: 'string',
							algorithm: 'AEAD_AES_256_CBC_HMAC_SHA_512-Deterministic'
						  }
						}
					  }
					}
				  }
				]
			  }
			}
		  }
		}
	  }
	}
});