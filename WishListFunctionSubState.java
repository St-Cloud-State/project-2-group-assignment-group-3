package WarehouseFSM;
// Name: Carson Stallcup
// Group: 3
// File: WishListFunctionSubState.java
// Purpose: Sub-state for client wishlist operations.

import java.io.*;
import java.util.Iterator;
import java.util.Map;

public class WishListFunctionSubState {

    private static final int ADD_TO_WISHLIST = 1;
    private static final int SHOW_WISHLIST = 2;
    private static final int RETURN_TO_CLIENT_MENU = 0;

    private final Warehouse warehouse;
    private final Client currentClient;
    private final BufferedReader reader;

    public WishListFunctionSubState(Client currentClient) {
        this.warehouse = Warehouse.instance();
        this.currentClient = currentClient;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void run() {
        int command;
        do {
            showMenu();
            command = getCommand();
            switch (command) {
                case ADD_TO_WISHLIST:
                    addToWishlist();
                    break;
                case SHOW_WISHLIST:
                    showWishlist();
                    break;
                case RETURN_TO_CLIENT_MENU:
                    System.out.println("Returning to Client Menu...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (command != RETURN_TO_CLIENT_MENU);
    }

    private void showMenu() {
        System.out.println("\n--- Wishlist Functionalities ---");
        System.out.println(ADD_TO_WISHLIST + " : Add Item to Wishlist");
        System.out.println(SHOW_WISHLIST + " : View Wishlist");
        System.out.println(RETURN_TO_CLIENT_MENU + " : Back to Client Menu");
    }

    private int getCommand() {
        try {
            System.out.print("Enter choice: ");
            String line = reader.readLine();
            return Integer.parseInt(line.trim());
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input.");
            return -1;
        }
    }

    private void addToWishlist() {
        try {
            System.out.print("Enter product ID: ");
            String productId = reader.readLine().trim();
            System.out.print("Enter quantity: ");
            int quantity = Integer.parseInt(reader.readLine());

            Map<String, Object> response =
                    warehouse.addToWishlist(productId, quantity, currentClient.getId());

            if ("success".equals(response.get("status"))) {
                System.out.println("Added to wishlist successfully.");
            } else {
                System.out.println("Failed to add: " + response.get("message"));
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private void showWishlist() {
        Wishlist wishlist = warehouse.getWishlist(currentClient.getId());
        if (wishlist == null || !wishlist.getItems().hasNext()) {
            System.out.println("Wishlist is empty.");
            return;
        }

        System.out.println("\n--- Wishlist ---");
        Iterator<WishlistItem> it = wishlist.getItems();
        while (it.hasNext()) {
            WishlistItem item = it.next();
            System.out.println(item);
        }
    }
}
