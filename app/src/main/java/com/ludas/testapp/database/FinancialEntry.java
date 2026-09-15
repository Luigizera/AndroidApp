package com.ludas.testapp.database;

public class FinancialEntry {
    public String date;
    public int type; // 0 = In, 1 = Out
    public int quantity;
    public double price; // sales price
    public double purchasePrice; // cost price

    public FinancialEntry(String date, int type, int quantity, double price, double purchasePrice) {
        this.date = date;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.purchasePrice = purchasePrice;
    }
}
