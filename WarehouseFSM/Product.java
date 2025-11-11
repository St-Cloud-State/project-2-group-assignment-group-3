// Name: Sohaib Akram
// Group 3
// StarID: ho7246ag
// File: product.java
// Purpose: This class contains all information pertaining to each product
// that is within the product inventory. 

import java.util.UUID;

public class Product {
    private String id;
    private String name;
    private int amount;
    private float salePrice;
    private Waitlist waitlist;

    public Product(String name, int amount, float salePrice) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.amount = amount;
        this.salePrice = salePrice;
        this.waitlist = new Waitlist();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAmount() { return amount; }
    public float getSalePrice() { return salePrice; }

    public void setName(String name) { this.name = name; }
    public void setAmount(int amount) { this.amount = amount; }
    public void setsalePrice(float salePrice) { this.salePrice = salePrice; }

    public Waitlist getWaitlist() { return this.waitlist; }

    public void updateQuantity(int quantity) {
        this.amount += quantity;
    }


   @Override
    public String toString() {
        return String.format("Product[%s] %s, Quantity: %d, Price: $%.2f", id, name, amount, salePrice);
    }
}
