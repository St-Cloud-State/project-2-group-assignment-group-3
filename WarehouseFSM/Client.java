// Name: Carson Stallcup
// Group 3
// StarID: po1278cm
// File: Client.java
// Purpose: This class contains all information pertaining to each user. 

import java.util.ArrayList;
import java.util.UUID; 

public class Client {
    private String id;
    private String name;
    private String address;
    private double balance;
    private Wishlist wishlist;
    private InvoiceList invoices;

    public Client(String name, String address) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.address = address;
        this.balance = 0.0;
        this.wishlist = new Wishlist();
        this.invoices = new InvoiceList();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getBalance() {
        return balance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void updateBalance(double amount) {
        this.balance += amount;
    }

    public void pay(double amount) {
        if (amount > 0) {
            this.balance -= amount;
            System.out.println("Payment of $" + amount + " applied to client: " + this.name);
        } else {
            System.out.println("Invalid payment amount. Payment must be greater than zero.");
        }
    }

    public Wishlist getWishlist() {
        return this.wishlist;
    }

    public InvoiceList getInvoices() {
        return this.invoices;
    }

    @Override
    public String toString() {
        return String.format("Client[%s] %s, %s, Balance: $%.2f",
                id, name, address, balance);
    }
}
