package com.mongoapp.view;

import java.time.LocalDate;

public class ExpenseEntry {
    private LocalDate date; 
    private String description; 
    private double amount;
    private String category; 
    
    public ExpenseEntry(LocalDate date, String description, double amount, String category) {
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Date: " + date + 
               ", Description: " + description + 
               ", Amount: $" + amount + 
               ", Category: " + category;
    }
}
