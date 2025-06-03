package com.mongoapp.view;

import org.bson.Document;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Expense {
    private String description;
    private static double income ;
    private double amountSpent; 
    private static double currentAmt;
    private List<ExpenseEntry> expenses;
    private double target;

    private Map<String, Double> expensesByCategory = new HashMap<>();

    public Expense(String user) {
        this.description = "";
        this.amountSpent = 0.0; 
        this.target = Db.getTarget(user);
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
            System.out.println("Current Balance: $" + currentAmt + ", Target Savings: " + target);
        }
    }
public static List<ExpenseEntry> getExpenseEntriesFromDB(String username) {
    List<ExpenseEntry> expenseEntries = new ArrayList<>();
    List<Document> expenses = Db.getExpenses(username);  

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    for (Document doc : expenses) {
        String category = doc.getString("category");
        String description = doc.getString("description");
        double amount = doc.getDouble("amount");

        LocalDate date;
        if (doc.containsKey("date")) {
            String dateStr = doc.getString("date");
            LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
            date = dateTime.toLocalDate();
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
        System.out.println("Expenses" +total);
        return total;
    }

    public double calculatePercentSaved(String user) {
        if (income <= 0) return 0; 
        System.out.println(1 - (getTotalExpenses(user) / income));
        return (1 - (getTotalExpenses(user) / income)) * 100; 
    }

    public double getTarget(String user) {
        return Db.getTarget(user);
    }

    public boolean isNearSpendingLimit(String user) {
        if (target <= 0) return false; 
        double spentPercentage = (getIncome(user)-target) * 0.9;
        return getTotalExpenses(user)>=spentPercentage;
    }

    
    public static void setIncome(double income,String user) {
        Expense.income += income;
        Expense.currentAmt += income; 
        Db.addIncome(user, income);
    }

    public static double getIncome(String user) {
        System.out.println("Income" + Db.getIncome(user));
        return income = Db.getIncome(user);
    }

    public void setTarget(double targetAmount,String user) {
        this.target+=Db.getTarget(user);
        this.target += targetAmount;
        Db.setTarget(user, targetAmount);
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
