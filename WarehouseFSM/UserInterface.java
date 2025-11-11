import java.util.*;
import java.io.*;

public class UserInterface {
    private static UserInterface userInterface;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;

    private static final String PURCHASE = "Purchase";
    private static final String REMOVE = "Remove";
    private static final String LEAVE = "Leave";

    // Menu options
    private static final int EXIT = 0;
    private static final int ADD_CLIENT = 1;
    private static final int ADD_PRODUCT = 2;
    private static final int SEARCH_CLIENT = 3;
    private static final int SEARCH_PRODUCT = 4;
    private static final int GET_CLIENTS = 5;
    private static final int GET_PRODUCTS = 6;
    private static final int GET_WISHLIST = 7;
    private static final int ADD_TO_WISHLIST = 8;
    private static final int PROCESS_CLIENT_ORDER = 9;
    private static final int RECEIVE_SHIPMENT = 10;
    private static final int RECEIVE_PAYMENT = 11;
    private static final int GET_WAITLIST = 12; 
    private static final int GET_INVOICES = 13;

    private UserInterface() {
        warehouse = Warehouse.instance();
    }

    public static UserInterface instance() {
        if (userInterface == null)
            userInterface = new UserInterface();
        return userInterface;
    }

    public static void main(String[] args) {
        UserInterface.instance().run();
    }

    public void run() {
        int command;
        System.out.println("Welcome to the Warehouse Management System!");

        do {
            showMenu();
            command = getCommand();

            switch (command) {
                case ADD_CLIENT:
                    addClient();
                    break;
                case ADD_PRODUCT:
                    addProduct();
                    break;
                case SEARCH_CLIENT:
                    searchClient();
                    break;
                case SEARCH_PRODUCT:
                    searchProduct();
                    break;
                case GET_CLIENTS:
                    getClients();
                    break;
                case GET_PRODUCTS:
                    getProducts();
                    break;
                case GET_WISHLIST:
                    getWishlist();
                    break;
                case ADD_TO_WISHLIST:
                    addToWishlist();
                    break;
                case PROCESS_CLIENT_ORDER:
                    processClientOrder();
                    break;
                case RECEIVE_SHIPMENT:
                    receiveShipment();
                    break;
                case RECEIVE_PAYMENT:
                    receivePayment();
                    break;
                case GET_WAITLIST:
                    getWaitlist();
                    break;
                case GET_INVOICES:
                    getInvoices();
                    break;
                case EXIT:
                    System.out.println("Exiting the program.");
                    break;
                default:
                    System.out.println("Invalid command.");
            }
        } while (command != EXIT);
    }

    private void showMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println(ADD_CLIENT + " : Add Client");
        System.out.println(ADD_PRODUCT + " : Add Product");
        System.out.println(SEARCH_CLIENT + " : Search Client by ID");
        System.out.println(SEARCH_PRODUCT + " : Search Product by ID");
        System.out.println(GET_CLIENTS + " : Get All Clients");
        System.out.println(GET_PRODUCTS + " : Get All Products");
        System.out.println(GET_WISHLIST + " : Get Client Wishlist");
        System.out.println(ADD_TO_WISHLIST + " : Add Product to Client Wishlist");
        System.out.println(PROCESS_CLIENT_ORDER + " : Process Client Order");
        System.out.println(RECEIVE_SHIPMENT + " : Receive Shipment");
        System.out.println(RECEIVE_PAYMENT + " : Receive Payment");
        System.out.println(GET_WAITLIST + " : Get Waitlist");
        System.out.println(GET_INVOICES + " : Get Invoices");
        System.out.println(EXIT + " : Exit");
    }

    private int getCommand() {
        int command = -1;
        try {
            System.out.print("Enter your choice: ");
            command = Integer.parseInt(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
        return command;
    }

    private void addClient() {
        try {
            System.out.print("Enter client name: ");
            String name = reader.readLine().trim();
            System.out.print("Enter client address: ");
            String address = reader.readLine().trim();

            Client client = warehouse.addClient(name, address);
            if (client != null) {
                System.out.println("Client added successfully! ID: " + client.getId());
            } else {
                System.out.println("Failed to add client.");
            }
        } catch (IOException e) {
            System.out.println("Error reading input.");
        }
    }

    private void addProduct() {
        try {
            System.out.print("Enter product name: ");
            String name = reader.readLine().trim();
            System.out.print("Enter product price: ");
            float price = Float.parseFloat(reader.readLine());
            System.out.print("Enter product quantity: ");
            int quantity = Integer.parseInt(reader.readLine());

            Product product = warehouse.addProduct(name, price, quantity);
            if (product != null) {
                System.out.println("Product added successfully! ID: " + product.getId());
            } else {
                System.out.println("Failed to add product.");
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private void searchClient() {
        try {
            System.out.print("Enter client ID: ");
            String id = reader.readLine().trim();
            Client client = warehouse.searchClient(id);
            if (client != null) {
                System.out.println("Client found: " + client);
            } else {
                System.out.println("Client not found.");
            }
        } catch (IOException e) {
            System.out.println("Error reading input.");
        }
    }

    private void searchProduct() {
        try {
            System.out.print("Enter product ID: ");
            String id = reader.readLine().trim();
            Product product = warehouse.searchProduct(id);
            if (product != null) {
                System.out.println("Product found: " + product);
            } else {
                System.out.println("Product not found.");
            }
        } catch (IOException e) {
            System.out.println("Error reading input.");
        }
    }

    private void getClients() {
        Iterator<Client> iterator = warehouse.getClients();
        if (!iterator.hasNext()) {
            System.out.println("No clients found.");
            return;
        }
        System.out.println("--- Clients List ---");
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    private void getProducts() {
        Iterator<Product> iterator = warehouse.getProducts();
        if (!iterator.hasNext()) {
            System.out.println("No products found.");
            return;
        }
        System.out.println("--- Products List ---");
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    private void getWishlist() {
        try {
            System.out.print("Enter client ID: ");
            String clientId = reader.readLine().trim();
            Wishlist wishlist = warehouse.getWishlist(clientId);

            if (wishlist == null || !(wishlist.getItems().hasNext())) {
                System.out.println("Wishlist is empty or client not found.");
                return;
            }

            System.out.println("--- Wishlist ---");

            Iterator<WishlistItem> it  = wishlist.getItems();

            while (it.hasNext()) {
                Item item = it.next();
                System.out.println(item);
            }

        } catch (IOException e) {
            System.out.println("Error reading input.");
        }
    }

    private void addToWishlist() {
        try {
            System.out.print("Enter client ID: ");
            String clientId = reader.readLine().trim();
            System.out.print("Enter product ID: ");
            String productId = reader.readLine().trim();
            System.out.print("Enter quantity: ");
            int quantity = Integer.parseInt(reader.readLine());

            Map<String, Object> response = warehouse.addToWishlist(productId, quantity, clientId);
            if (response.get("status").equals("success")) {
                System.out.println("Added to wishlist. Current quantity: " + response.get("current_quantity"));
            } else {
                System.out.println("Failed to add to wishlist: " + response.get("message"));
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private void processClientOrder() {
        try {
            System.out.print("Enter Client ID: ");
            String clientId = reader.readLine().trim();

            Client client = warehouse.searchClient(clientId);

            if (client == null) {
                System.out.println("Client not found. Exit");
                return;
            }

            InvoiceList client_invoices = client.getInvoices();
            Invoice invoice = new Invoice(clientId);

            Wishlist wishlist = client.getWishlist();
            Iterator<WishlistItem> iterator = wishlist.getItems();
            if (!iterator.hasNext()) {
                System.out.println("Wishlist is empty");
                return;
            }
            System.out.println(("--- Wishlist Items ---"));
            while (iterator.hasNext()) {
                WishlistItem item = iterator.next();
                System.out.println(item);
                System.out.println("Purchase / Remove / Leave");
                System.out.print("Enter response: ");
                String response = reader.readLine().trim();

                switch (response) {
                    case PURCHASE:
                        System.out.print("Enter quantity to be ordered: ");
                        int quantity = Integer.parseInt(reader.readLine());
                        InvoiceItem invoiceItem = warehouse.order(item.getProductId(), quantity, clientId);
                        invoice.addItem(invoiceItem);
                        iterator.remove();
                        break;
                    case REMOVE:
                        iterator.remove();
                        break;
                    case LEAVE:
                        break;
                }
            }
            
            client_invoices.insertItem(invoice);
            System.out.println(invoice.render());
        
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private void receiveShipment() {
        try {
            System.out.print("Enter product ID: ");
            String productId = reader.readLine().trim();
            System.out.print("Enter shipment quantity: ");
            int quantity = Integer.parseInt(reader.readLine());

            String result = warehouse.receiveShipment(productId, quantity);
            System.out.println(result);
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private void receivePayment() {
        try {
            System.out.print("Enter client ID: ");
            String clientId = reader.readLine().trim();
            System.out.print("Enter payment amount: ");
            float payment = Float.parseFloat(reader.readLine());

            warehouse.receivePayment(clientId, payment);
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private void getWaitlist() {
        try {
            System.out.print("Enter product ID: ");
            String productId = reader.readLine().trim();
            Waitlist waitlist = warehouse.getWaitlist(productId);

            if (waitlist == null || !(waitlist.getItems().hasNext())) {
                System.out.println("Waitlist is empty or client not found.");
                return;
            }

            System.out.println("--- Waitlist ---");

            Iterator<WaitlistItem> it  = waitlist.getItems();

            while (it.hasNext()) {
                Item item = it.next();
                System.out.println(item);
            }

        } catch (IOException e) {
            System.out.println("Error reading input.");
        }
    }

    private void getInvoices() {
        try {
            System.out.print("Enter client ID: ");
            String clientId = reader.readLine().trim();
            InvoiceList invoices = warehouse.getInvoices(clientId);

            if (invoices == null || !(invoices.getItems().hasNext())) {
                System.out.println("Client does not have any invoice.");
                return;
            }

            System.out.println("--- Invoices ---");

            Iterator<Invoice> it  = invoices.getItems();

            while (it.hasNext()) {
                Invoice item = it.next();
                System.out.println(item.render());
            }

        } catch (IOException e) {
            System.out.println("Error reading input.");
        }
    }
}
