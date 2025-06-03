package com.mongoapp;

import com.mongodb.client.*;
import org.bson.Document;
import com.mongoapp.view.Main;

public class App{
    public static void main(String[] args) {
      
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = mongoClient.getDatabase("NoBrokeZone");
            MongoCollection<Document> col = db.getCollection("testUsers");
            col.insertOne(new Document("testKey", "testValue"));
            System.out.println("MongoDB Connected and inserted!");
            javax.swing.SwingUtilities.invokeLater(() -> {
                new Main("No broke zone"); 
            });
        }
    }
}
