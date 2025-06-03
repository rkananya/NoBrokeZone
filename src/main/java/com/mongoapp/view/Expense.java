package com.mongoapp.view;

import org.bson.Document;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class Expense {
    private String description;
    private static double income ;
    private double amountSpent; 
    private static double currentAmt;
    private List<ExpenseEntry> expenses;
    private double target;

    private Map<String, Double> expensesByCategory = new HashMap<>();

    public Expense() {
        this.description = "";
        this.amountSpent = 0.0; 
        this.target = 0.0;
        this.expenses = new ArrayList<>();
    }

    private void checkSavingsProgress() {
        if (target <= 0) {
            System.out.println("Target savings not set.");
            return;
        }
        double targetAmount = income - target; 
        double spentAmount = income - currentAmt; 

        if (spentAmount >= targetAmount * 0.9) {
            System.out.println("Warning: You have spent 90% of your target savings amount!");
            System.out.println("Current Balance: $" + currentAmt + ", Target Savings: $" + target);
        }
    }
public static List<ExpenseEntry> getExpenseEntriesFromDB(String username) {
    List<ExpenseEntry> expenseEntries = new ArrayList<>();
    List<Document> expenses = Db.getExpenses(username);  // uses embedded expenses array

    for (Document doc : expenses) {
        String category = doc.getString("category");
        String description = doc.getString("description");
        double amount = doc.getDouble("amount");

        LocalDate date;
        if (doc.containsKey("date")) {
            date = LocalDate.parse(doc.getString("date"));  // assuming ISO format
        } else if (doc.containsKey("timestamp")) {
            long timestamp = doc.getLong("timestamp");
            date = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
        } else {
            date = LocalDate.now(); // fallback
        }

        expenseEntries.add(new ExpenseEntry(date, description, amount, category));
    }

    return expenseEntries;
}

    public double getTotalExpenses(String user) {
         List<ExpenseEntry> expenseEntries = getExpenseEntriesFromDB(user);
    double total = 0.0;

    for (ExpenseEntry entry : expenseEntries) {
        total += entry.getAmount();
    }

    return total;
    }

    public double calculatePercentSaved(String user) {
        if (income <= 0) return 0; 
        return (1 - (getTotalExpenses(user) / income)) * 100; 
    }

    public double getTarget() {
        return target;
    }

    public boolean isNearSpendingLimit(String user) {
        if (target <= 0) return false; 
        double spentPercentage = (getIncome(user)-target) * 0.9;
        return getTotalExpenses(user)>=spentPercentage;
    }

    
    public static void setIncome(double income) {
        Expense.income += income;
        Expense.currentAmt += income; 
    }

    public static double getIncome(String user) {
        return income = Db.getIncome(user);
    }

    public void setTarget(double targetAmount) {
        this.target += targetAmount;
    }

    public void addExpense(String user,String description, String category, double amountSpent) {
        if (amountSpent > currentAmt) {
            throw new IllegalArgumentException("Amount spent cannot be greater than current amount");
        }

        currentAmt -= amountSpent; 
        checkSavingsProgress(); 
        ExpenseEntry newEntry = new ExpenseEntry(LocalDate.now(), description, amountSpent, category);
        expenses.add(newEntry);
        expensesByCategory.put(category, expensesByCategory.getOrDefault(category, 0.0) + amountSpent);

        System.out.println("Expense added: " + newEntry);
        Db.addExpense(user, category, description, amountSpent);
    }

}
