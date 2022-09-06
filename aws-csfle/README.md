# aws-csfle
An application testing MongoDB CSFLE capabilities utilizing either automatic or explicit encryption options.

## Assumptions
- Using enterprise level MongoDB, need this to utilize `mongocryptd`
- Using `AWS` KMIP provider


## Constant Replace
- search value: `"<key_arn>"`, replace with: `your AWS KMIP key arn`
- search value: `"<key_region>"`, replace with: `your AWS KMIP key region`
- search value: `"<mongo_uri>"`, replace with: `your mongo uri`
- search value: `"<ACCESS_KEY_ID>"`, replace with: `your KMS access key id`
- search value: `"<SECRET_ACCESS_KEY>"`, replace with: `your KMS access key`
- search value: `"<SESSION_TOKEN>"`, replace with: `your KMS role token`
- search value: `"<DEK_ID>"`, replace with: `your CSFLE dekId`
- search value: `"<DEK_UUID>"`, replace with: `your CSFLE dekId MongoDB UUID`



## CSFLE Encrytion Key
If you do not have an encryption key yet, be sure to run the app found in the /scripts folder - `aws-csfle/src/main/java/com/mdb/ps/scripts/MakeDataKey.java`

Copy the output value from
`"DataKeyId [base64]: "`

This will create a record in our keyVault (`__encryption.__keyVault`)

If you already have an encryption key, be sure to replace all occurances of the hard-coded keyvault, search value: `"__encryption.__keyVault"`


## Server-side schema validation
To apply server-side schema validation, connect to a mongo client and run the InsertJSONSchema javascript file - `aws-csfle/src/main/java/com/mdb/ps/scripts/InsertJSONSchema.js`


## Running the App
To run the app, run `aws-csfle/src/main/java/com/mdb/ps/App.java`
- automatic csfle: set `isExplicitEncryption` to `false`
- explicit csfle: set `isExplicitEncryption` to `true`
- insert nested document: set `isArray` to `false`
- insert nested array of documents: set `isArray` to `true`


## References
- https://www.mongodb.com/docs/manual/core/csfle/
  - https://www.mongodb.com/docs/manual/core/csfle/quick-start/#std-label-csfle-quick-start
- https://www.mongodb.com/docs/drivers/java/sync/current/fundamentals/csfle/
- https://www.mongodb.com/docs/manual/reference/operator/query/jsonSchema/
