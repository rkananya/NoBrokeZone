package com.mongoapp.view;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Db {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DB_NAME = "NoBrokeZone";
    private static final MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
    private static final MongoDatabase database = mongoClient.getDatabase(DB_NAME);
    private static final MongoCollection<Document> usersCollection = database.getCollection("users");

    public static MongoCollection<Document> getUsersCollection() {
        return usersCollection;
    }
    public static String getUser(String username) {
        Document user = usersCollection.find(Filters.eq("username", username)).first();
        return user != null && user.containsKey("username") ? user.getString("username") : null;
    }

  
    public static void setBalance(String username, double balance) {
        usersCollection.updateOne(
            Filters.eq("username", username),
            new Document("$set", new Document("balance", balance))
        );
    }

    public static double getBalance(String username) {
        Document user = usersCollection.find(Filters.eq("username", username)).first();
        return user != null && user.containsKey("balance") ? user.getDouble("balance") : 0.0;
    }

  public static List<String[]> getAllExpenses(String username) {
    List<String[]> expensesList = new ArrayList<>();
    List<Document> expenses = getExpenses(username);

    for (Document doc : expenses) {
        String category = doc.getString("category");
        String description = doc.getString("description");
        String amount = String.valueOf(doc.getDouble("amount"));
        String date = doc.containsKey("date") ? doc.getString("date") : "N/A";

        expensesList.add(new String[]{date, category, description, amount});
    }

    return expensesList;
}


    public static void setPercentSaved(String username, double percent) {
        usersCollection.updateOne(
            Filters.eq("username", username),
            new Document("$set", new Document("percentSaved", percent))
        );
    }

    public static double getPercentSaved(String username) {
        Document user = usersCollection.find(Filters.eq("username", username)).first();
        return user != null && user.containsKey("percentSaved") ? user.getDouble("percentSaved") : 0.0;
    }



    public static void addExpense(String username, String category, String description, double amount) {
        Document expense = new Document("category", category)
            .append("description", description)
            .append("amount", amount)
            .append("timestamp", System.currentTimeMillis());

        usersCollection.updateOne(
            Filters.eq("username", username),
            new Document("$push", new Document("expenses", expense))
        );
    }

    public static List<Document> getExpenses(String username) {
        Document user = usersCollection.find(Filters.eq("username", username)).first();
        if (user != null && user.containsKey("expenses")) {
            return (List<Document>) user.get("expenses");
        }
        return new ArrayList<>();
    }
    public static Map<String, Double> getExpenseData(String username) {
    List<Document> expenses = getExpenses(username);
    Map<String, Double> categoryTotals = new HashMap<>();

    for (Document expense : expenses) {
        String category = expense.getString("category");
        double amount = expense.getDouble("amount");

        categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + amount);
    }

    return categoryTotals;
}

    public static void addIncome(String username, double incomeAmount) {
        usersCollection.updateOne(
            Filters.eq("username", username),
            new Document("$inc", new Document("income", incomeAmount))
        );
        System.out.println(username+"added income in db"+incomeAmount);
    }

    public static double getIncome(String username) {
        Document user = usersCollection.find(Filters.eq("username", username)).first();
        return user != null && user.containsKey("income") ? user.getDouble("income") : 0.0;
    }



    public static String createUserIfNotExists(String username) {
        if (usersCollection.find(Filters.eq("username", username)).first() == null) {
            Document newUser = new Document("username", username)
                .append("balance", 0.0)
                .append("percentSaved", 0.0)
                .append("income", 0.0)
                .append("expenses", new ArrayList<>());
            usersCollection.insertOne(newUser);
            return ("Added!");
        }
        else{
            return (username);
        }
    }
}
