// Name: Carson Stallcup
// Group: 3
// File: ClientMenuState.java
// Purpose: Provides the client-side control panel for interacting with the Warehouse system.

import java.io.*;
import java.util.*;

public class ClientMenuState extends State {
    private static ClientMenuState ClientMenuState;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private Client currentClient;

    //Fixed up requirements.
    private static final int SHOW_DETAILS = 1;
    private static final int SHOW_PRODUCTS = 2;
    private static final int SHOW_TRANSACTIONS = 3;
    private static final int WISHLIST_FUNCTIONALITIES = 4;
    private static final int PLACE_ORDER = 5;
    private static final int LOGOUT = 0;

    private ClientMenuState() {
        warehouse = Warehouse.instance();
    }

    public static ClientMenuState instance() {
        if (ClientMenuState == null)
            ClientMenuState = new ClientMenuState();
        return ClientMenuState;
    }

    public void run() {
        ContextManager  ctx = ContextManager.instance();
        String currentClientId = ctx.getSession().userId();
        currentClient = warehouse.searchClient(currentClientId);
        int command;
        System.out.println("\nWelcome, " + currentClient.getName() + "!");
        do {
            showMenu();
            command = getCommand();
            switch (command) {
                    //Options based on requirements.
                case SHOW_DETAILS:
                    showClientDetails();
                    break;
                case SHOW_PRODUCTS:
                    showProducts();
                    break;
                case SHOW_TRANSACTIONS:
                    showTransactions();
                    break;
                case WISHLIST_FUNCTIONALITIES:
                    // Enter wishlist sub-state
                    WishListFunctionSubState wishlistState =
                            new WishListFunctionSubState(currentClient);
                    wishlistState.run();
                    break;
                case PLACE_ORDER:
                    placeOrder();
                    break;
                case LOGOUT:
                    System.out.println("Logging out...");
                    break;

                    //In case inputs aren't valid.
                default:
                    System.out.println("Invalid choice.");
            }
        } while (command != LOGOUT);
        ctx.handleLogout();
    }

    private void showMenu() {
        System.out.println("\n--- Client Menu ---");
        System.out.println(SHOW_DETAILS + " : Show My Details");
        System.out.println(SHOW_PRODUCTS + " : View Product List");
        System.out.println(SHOW_TRANSACTIONS + " : View Transactions");
        System.out.println(WISHLIST_FUNCTIONALITIES + " : Wishlist Functionalities");
        System.out.println(PLACE_ORDER + " : Place an Order");
        System.out.println(LOGOUT + " : Logout");
    }

    private int getCommand() {
        try {
            System.out.print("Enter choice: ");
            return Integer.parseInt(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input.");
            return -1;
        }
    }

    private void showClientDetails() {
        System.out.println("\n--- Client Details ---");
        System.out.println("ID: " + currentClient.getId());
        System.out.println("Name: " + currentClient.getName());
        System.out.println("Address: " + currentClient.getAddress());
        System.out.println("Balance: $" + currentClient.getBalance());
    }

    private void showProducts() {
        Iterator<Product> iterator = warehouse.getProducts();
        if (!iterator.hasNext()) {
            System.out.println("No products available.");
            return;
        }
        System.out.println("\n--- Product List ---");
        while (iterator.hasNext()) {
            Product product = iterator.next();
            System.out.printf("ID: %s | %s | $%.2f | Qty: %d%n",
                    product.getId(), product.getName(),
                    product.getSalePrice(), product.getAmount());
        }
    }

    private void showTransactions() {
        InvoiceList invoices = currentClient.getInvoices();
        if (invoices == null || !invoices.getItems().hasNext()) {
            System.out.println("No transactions found.");
            return;
        }
        System.out.println("\n--- Transaction History ---");
        Iterator<Invoice> iterator = invoices.getItems();
        while (iterator.hasNext()) {
            System.out.println(iterator.next().render());
        }
    }

    private void placeOrder() {
        Wishlist wishlist = currentClient.getWishlist();
        if (wishlist == null || !wishlist.getItems().hasNext()) {
            System.out.println("Wishlist is empty, nothing to order.");
            return;
        }
        Invoice invoice = new Invoice(currentClient.getId());
        Iterator<WishlistItem> iterator = wishlist.getItems();

        System.out.println("\n--- Wishlist Items ---");
        while (iterator.hasNext()) {
            WishlistItem item = iterator.next();
            System.out.println(item);
            try {
                System.out.print("Enter quantity to purchase (0 to skip): ");
                int qty = Integer.parseInt(reader.readLine());
                if (qty > 0) {
                    InvoiceItem invoiceItem =
                            warehouse.order(item.getProductId(), qty, currentClient.getId());
                    invoice.addItem(invoiceItem);
                    iterator.remove();
                }
            } catch (IOException | NumberFormatException e) {
                System.out.println("Invalid input, skipping item.");
            }
        }

        currentClient.getInvoices().insertItem(invoice);
        System.out.println("\n--- Order Summary ---");
        System.out.println(invoice.render());
    }
}
