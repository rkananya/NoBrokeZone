package com.mongoapp.view;
import org.bson.Document;
import java.util.List;
public class dbTest {
  public static void main(String[] args) {
        String testUser = "Ananya";
        Db.getUsersCollection().insertOne(
            new Document("username", testUser)
                .append("income", 0.0)
                .append("expenses", List.of())
        );

        Db.addIncome(testUser, 10000);
        Db.addExpense(testUser, "Food", "Cafe", 300);
        Db.addExpense(testUser, "Clothing", "Zudio", 500);

        double income = Db.getIncome(testUser);
        System.out.println("Income: Rs" + income);

        List<Document> expenses = Db.getExpenses(testUser);
        System.out.println("Expenses:");
        for (Document doc : expenses) {
            System.out.println(doc.toJson());
        }

        double percent = Db.getPercentSaved(testUser);
        System.out.printf("Percent Saved: %.2f%%\n", percent);
    }   
}
