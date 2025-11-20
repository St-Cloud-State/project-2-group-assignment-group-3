import java.util.Iterator;
import java.util.Scanner;

public final class ManagerMenuState extends State {
    private static ManagerMenuState instance;
    private final Scanner scanner = new Scanner(System.in);
    private final Warehouse warehouse = Warehouse.instance();

    private ManagerMenuState() {}

    public static ManagerMenuState instance() {
        if (instance == null) instance = new ManagerMenuState();
        return instance;
    }

    @Override
    public void run() {
        while (true) {
            showMenu();
            String line = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice.");
                continue;
            }

            switch (choice) {
                case 1 : addProduct(); break;
                case 2 : displayWaitlist(); break;
                case 3 : receiveShipment(); break;
                case 4 : becomeClerk(); break;
                case 5 :  logout(); return;  
                default : System.out.println("Invalid choice."); break;
            }
        }
    }

    private void showMenu() {
        System.out.println("\n--- Manager Menu ---");
        System.out.println("1. Add Product");
        System.out.println("2. Display Waitlist for Product");
        System.out.println("3. Receive Shipment");
        System.out.println("4. Become Clerk");
        System.out.println("5. Logout");
        System.out.print("Choose: ");
    }

 
    private void addProduct() {
        try {
            System.out.print("Name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Unit Price: ");
            float price = Float.parseFloat(scanner.nextLine().trim());

            System.out.print("Initial Quantity: ");
            int qty = Integer.parseInt(scanner.nextLine().trim());

            Product p = warehouse.addProduct(name, price, qty);
            if (p != null) {
                System.out.println("Product added! ID: " + p.getId());
                System.out.println(p);
            } else {
                System.out.println("Failed to add product (check inputs).");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
        }
    }

    private void displayWaitlist() {
        System.out.print("Enter product ID: ");
        String id = scanner.nextLine().trim();

        Product p = warehouse.searchProduct(id);
        if (p == null) {
            System.out.println("Product not found.");
            return;
        }

        Waitlist wl = p.getWaitlist();
        if (wl == null) {
            System.out.println("No waitlist for this product.");
            return;
        }

        Iterator<WaitlistItem> it = wl.getItems();
        if (it == null || !it.hasNext()) {
            System.out.println("Waitlist is empty.");
            return;
        }

        System.out.println("--- Waitlist for " + id + " (" + p.getName() + ") ---");
        while (it.hasNext()) {
            System.out.println(it.next()); 
        }
    }

    private void receiveShipment() {
        try {
            System.out.print("Enter product ID: ");
            String id = scanner.nextLine().trim();

            System.out.print("Quantity received: ");
            int qty = Integer.parseInt(scanner.nextLine().trim());

            String result = warehouse.receiveShipment(id, qty); 
            System.out.println(result);
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity.");
        }
    }


    private void becomeClerk() {
        Session session = new Session(Enums.State.CLERK, "clerk");
        ContextManager.instance().addSession(session);
        ContextManager.instance().changeState(Enums.Transition.TO_CLERK);
    }

    private void logout() {
        ContextManager.instance().handleLogout();
    }

    public void help() {
        System.out.println("Manager: add product, view waitlist, receive shipment, become clerk, logout.");
    }
}
