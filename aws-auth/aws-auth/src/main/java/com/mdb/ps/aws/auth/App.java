package com.mdb.ps.aws.auth;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.Document;

import com.mongodb.MongoCredential;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     * @throws UnsupportedEncodingException
     */
    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println("Hello World!");

        String username_raw = "<key>";
        String password_raw = "<secret>";
        String token_raw = "<token>";
        String encoder = "UTF-8";

        String username = URLEncoder.encode(username_raw, encoder);
        String password = URLEncoder.encode(password_raw, encoder);
        String token = URLEncoder.encode(token_raw, encoder);

        ServerApi serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build();

        // MongoCredential

        try {
            MongoCredential credential = MongoCredential.createAwsCredential(username_raw, password_raw.toCharArray())
                .withMechanismProperty("AWS_SESSION_TOKEN",  token_raw);
            ConnectionString connectionString = new ConnectionString("mongodb+srv://<ATLAS_VIP>");

            System.out.println("MongoCredential --> " + credential.toString());

            MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .credential(credential)
                .serverApi(serverApi)
                .build();

            try (MongoClient mongoClient = MongoClients.create(settings)) {
                try {
                    // Send a ping to confirm a successful connection
                    Document document = mongoClient.getDatabase("<DB>").getCollection("<COLLECTION>").find().explain();
                    System.out.println(document.toJson());
                    System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
                } catch (MongoException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Explicit connectionstring

        try {
            String connectionString_Explicit = "mongodb+srv://"+ username +":" + password + "@<ATLAS_VIP>/?authSource=%24external&authMechanism=MONGODB-AWS&retryWrites=true&w=majority&authMechanismProperties=AWS_SESSION_TOKEN:" + token;

            MongoClientSettings settings_Explicit = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(connectionString_Explicit))
                    .serverApi(serverApi)
                    .build();
            // Create a new client and connect to the server
            try (MongoClient mongoClient = MongoClients.create(settings_Explicit)) {
                try {
                    // Send a ping to confirm a successful connection
                    Document document = mongoClient.getDatabase("<DB>").getCollection("<COLLECTION>").find().explain();
                    System.out.println(document.toJson());
                    System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
                } catch (MongoException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
