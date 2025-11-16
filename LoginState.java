package WarehouseFSM;
import java.util.Scanner;

public final class LoginState extends State {
    private static LoginState instance;
    private final Warehouse warehouse = Warehouse.instance();
    private final Scanner scanner = new Scanner(System.in);

    private LoginState() {}

    public static LoginState instance() {
        if (instance == null) instance = new LoginState();
        return instance;
    }

    @Override
    public void run() {
        boolean done = false;
        while (!done) {
            showMenu();
            int choice = readInt("Choose: ");
            switch (choice) {
                case 1 -> done = becomeClient();
                case 2 -> done = loginAsRole("clerk", Enums.State.CLERK, Enums.Transition.TO_CLERK);
                case 3 -> done = loginAsRole("manager", Enums.State.MANAGER, Enums.Transition.TO_MANAGER);
                case 4 -> { ContextManager.instance().changeState(Enums.Transition.CLEAN_EXIT); return; }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void showMenu() {
        System.out.println("\n--- Login ---");
        System.out.println("1. Become Client");
        System.out.println("2. Become Clerk");
        System.out.println("3. Become Manager");
        System.out.println("4. Exit");
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Handle client login: asks for client ID and verifies it exists in the Warehouse's ClientList.
     * if sucesfull, it records the initial session and transitions to client state.
     */
    private boolean becomeClient() {
        String clientId = readLine("Enter client ID: ");
        if (warehouse.searchClient(clientId) != null) {
            ContextManager ctx = ContextManager.instance();
            // Record the first-entered state via session
            ctx.addSession(new Session(Enums.State.CLIENT, clientId));
            ctx.changeState(Enums.Transition.TO_CLIENT);
            return true; // leave login loop once state changes
        } else {
            System.out.println("No client found with ID: " + clientId);
            return false;
        }
    }

    /**
     * Generic username/password login for a staff role (clerk/manager).
     * Username and passwoed must both equal the role key, which is "clerk" or "manager").
     */
    private boolean loginAsRole(String roleKey, Enums.State state, Enums.Transition transition) {
        String username = readLine("Username: ");
        String password = readLine("Password: ");
        if (roleKey.equals(username) && roleKey.equals(password)) {
            ContextManager ctx = ContextManager.instance();
            ctx.addSession(new Session(state, username));
            ctx.changeState(transition);
            return true;
        } else {
            System.out.println("Invalid credentials for " + roleKey + ". Try again.");
            return false;
        }
    }

    public void help() {
        System.out.println("Login: choose client/clerk/manager or exit. Clerk/Manager require credentials (same word for user & pass).");
    }
}
