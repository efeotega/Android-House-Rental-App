package com.example.rent_it.Model;

public class TransactionData {
    private String reference;
    private String email;
    private String date;
    private int amount;

    public TransactionData() {
        // Default constructor required for calls to DataSnapshot.getValue(TransactionData.class)
    }

    public TransactionData(String reference, String email, String date, int amount) {
        this.reference = reference;
        this.email = email;
        this.date = date;
        this.amount = amount;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

