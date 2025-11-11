import java.util.*;

public class Warehouse {
    private ClientList clients;
    private ProductInventory products;
    private static Warehouse warehouse;

    private Warehouse() {
        this.clients = ClientList.instance();
        this.products = ProductInventory.instance();
    }

    public static Warehouse instance() {
        if (warehouse == null)
            warehouse = new Warehouse();
        return warehouse;
    }

    public Client addClient(String name, String address) {
        if (name == null || name.isBlank() || address == null || address.isBlank()) {
            return null;
        }
        Client newClient = new Client(name, address);
        if (this.clients.insertClient(newClient)) {
            return newClient;
        }
        return null;
    }

    public Product addProduct(String name, float salePrice, int amount) {
        if (name == null || name.isBlank() || salePrice <= 0 || amount <= 0) {
            return null;
        }

        Product newProduct = new Product(name, amount, salePrice);
        if (this.products.insertProduct(newProduct)) {
            return newProduct;
        }
        return null;
    }

    public Client searchClient(String clientId) {
        return this.clients.searchClient(clientId);
    }

    public Product searchProduct(String productId) {
        return this.products.searchProduct(productId);
    }

    public Iterator<Client> getClients() {
        return this.clients.getClients();
    }

    public Iterator<Product> getProducts() {
        return this.products.getProducts();
    }

    public Wishlist getWishlist(String clientID) {
        Client client = clients.searchClient(clientID);
        return client.getWishlist();
    }

    public Map<String, Object> addToWishlist(String productId, int quantity, String clientId) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "failure");

        try {
            Product product = this.searchProduct(productId);
            if (product == null) {
                response.put("message", "Product not found");
                return response;
            }

            Wishlist wishlist = this.getWishlist(clientId);
            if (wishlist == null) {
                response.put("message", "Client not found");
                return response;
            }

            WishlistItem item = wishlist.findItem(clientId);
            if (item != null) {
                item.updateQuantity(quantity);
            } else {
                item = new WishlistItem(productId, quantity);
                wishlist.insertItem(item);
            }

            response.put("status", "success");
            response.put("current_quantity", item.getQuantity());
        } catch (Exception e) {
            response.put("message", e.getMessage());
        }
        return response;
    }

 public String receiveShipment(String productId, int shipmentQuantity) {
    Product product = this.searchProduct(productId);
    if (product == null) {
        return "Product not found.";
    }

    
    System.out.println("Current stock for product '" + product.getName() + "' (ID: " 
        + product.getId() + "): " + product.getAmount() + " units.");

    System.out.println("Incoming shipment quantity: " + shipmentQuantity + " units.");

   
    product.updateQuantity(shipmentQuantity);

    Waitlist waitlist = product.getWaitlist();
    if (waitlist != null) {
        Iterator<WaitlistItem> iterator = waitlist.getItems();
        ArrayList<WaitlistItem> toRemove = new ArrayList<>();

        while (iterator.hasNext() && shipmentQuantity > 0) {
            WaitlistItem item = iterator.next();
            int requestedQty = item.getQuantity();
            String clientId = item.getClientId();
            Client client = searchClient(clientId); // Get client once here

            if (client == null) continue; // safety

            if (requestedQty <= shipmentQuantity) {
                // Full fulfillment
                InvoiceItem invoiceItem = order(productId, requestedQty, clientId);
                shipmentQuantity -= requestedQty;
                toRemove.add(item);

                // 🔹 Create and attach invoice
                if (invoiceItem != null) {
                    Invoice invoice = new Invoice(clientId);
                    invoice.addItem(invoiceItem);
                    client.getInvoices().insertItem(invoice);
                    System.out.println("Invoice created for client " + client.getName() +
                                       " for " + requestedQty + " units of " + product.getName() + ".");
                }

            } else if (shipmentQuantity > 0) {
                // Partial fulfillment
                InvoiceItem invoiceItem = order(productId, shipmentQuantity, clientId);

                if (invoiceItem != null) {
                    Invoice invoice = new Invoice(clientId);
                    invoice.addItem(invoiceItem);
                    client.getInvoices().insertItem(invoice);
                    System.out.println("Partial invoice created for client " + client.getName() +
                                       " for " + shipmentQuantity + " units of " + product.getName() + ".");
                }

                item.setQuantity(requestedQty - shipmentQuantity);
                shipmentQuantity = 0; // used all available stock
            }
        }

        // Remove fully fulfilled waitlist items
        for (WaitlistItem item : toRemove) {
            waitlist.removeItem(item);
        }
    }

        if (shipmentQuantity > 0) {
        System.out.println("Remaining " + shipmentQuantity +
    " units of '" + product.getName() + "' added to inventory after waitlist processing.");
    } else {
        System.out.println("All product quantity used to fulfill waitlists.");
    }

    return "Shipment processed successfully. Stock of the current product: " + product.getAmount();
}




    public Waitlist getWaitlist(String productId) {
        Product product = this.searchProduct(productId);
        return product.getWaitlist();
    }

    public InvoiceItem order(String productId, int quantity, String clientId) {
        Product product = this.searchProduct(productId);
        Client client = this.searchClient(clientId);
        InvoiceItem invoiceItem = null;
        int available = product.getAmount();

        if (quantity > available) {
            invoiceItem = new InvoiceItem(available, product.getName(), product.getSalePrice());
            float cost = available * product.getSalePrice();
            client.updateBalance(cost);
            int extra = quantity - available;
            product.updateQuantity(-available);
            Waitlist product_waitlist = product.getWaitlist();
            
            WaitlistItem item = product_waitlist.findItem(clientId);

            if (item == null) {
                WaitlistItem waitlistItem = new WaitlistItem(clientId, extra);
                product_waitlist.insertItem(waitlistItem);
            } else {
                item.updateQuantity(extra);
            }
        } else {
            invoiceItem = new InvoiceItem(quantity, product.getName(), product.getSalePrice());
            product.updateQuantity(-quantity);
            float cost = quantity * product.getSalePrice();
            client.updateBalance(cost);
        }

        return invoiceItem;
    }
    
    public void receivePayment(String clientId, double amount) {
        Client client = clients.searchClient(clientId);
        if (client != null) {
            client.pay(amount);
            System.out.println("Payment of $" + amount + " received from " + client.getName());
            System.out.println("Updated balance: $" + client.getBalance());
        } else {
            System.out.println("Client not found.");
        }
    }

    public InvoiceList getInvoices(String clientId) {
        Client client = this.searchClient(clientId);
        return client.getInvoices();
    }
}
