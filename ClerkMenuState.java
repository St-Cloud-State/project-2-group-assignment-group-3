package WarehouseFSM;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.StringTokenizer;

public class ClerkMenuState extends State {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private ContextManager context;
    private static ClerkMenuState instance;
    private static final int LOGOUT = 0;
    private static final int ADD_CLIENT = 1;
    private static final int DISPLAY_PRODUCTS = 2;
    private static final int DISPLAY_CLIENTS = 3;
    private static final int SHOW_CLIENTS_WITH_BALANCE = 4;
    private static final int RECORD_PAYMENT = 5;
    private static final int BECOME_CLIENT = 6;
    private static final int HELP = 7;
    
    private ClerkMenuState() {
        super();
        warehouse = Warehouse.instance();
    }

    public static ClerkMenuState instance() {
        if (instance == null) {
            instance = new ClerkMenuState();
          }
          return instance;
    }

    public String getToken(String prompt) {
        do {
            try {
                System.out.print(prompt);
                String line = reader.readLine();
                StringTokenizer tokenizer = new StringTokenizer(line,"\n\r\f");
                if (tokenizer.hasMoreTokens()) {
                    return tokenizer.nextToken();
                }
            } catch (IOException ioe) {
                System.exit(0);
            }
        } while (true);
    }

    public int getNumber(String prompt) {
        do {
            try {
                String item = getToken(prompt);
                Integer num = Integer.valueOf(item);
                return num.intValue();
            } catch (NumberFormatException nfe) {
                System.out.println("Please input a number ");
            }
        } while (true);
    }

    public int getCommand() {
        do {
            try {
                int value = Integer.parseInt(getToken("Enter command: "));
                if (value >= LOGOUT && value <= HELP) {
                    return value;
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Enter a number");
            }
        } while (true);
    }
    
    public void help() {
        System.out.println("Enter a number between 0 and 7 as explained below:");
        System.out.println(LOGOUT + ". to Logout");
        System.out.println(ADD_CLIENT + ". to add a client");
        System.out.println(DISPLAY_PRODUCTS + ". to display available products");
        System.out.println(DISPLAY_CLIENTS + ". to display all clients");
        System.out.println(SHOW_CLIENTS_WITH_BALANCE + ". to display clients with an outstanding balance");
        System.out.println(RECORD_PAYMENT + ". to record payment for a client");
        System.out.println(BECOME_CLIENT + ". to switch to the client menu");
        System.out.println(HELP + ". for help");
    }

    public void addClient() {
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

    public void getClients() {
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

    public void getClientsWithBalance() {
        Iterator<Client> iterator = warehouse.getClientsWithBalance();
        if (!iterator.hasNext()) {
            System.out.println("No clients found.");
            return;
        }
        System.out.println("--- Clients List ---");
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
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

    private boolean becomeClient() {
        String clientId = getToken("Please input the client id: ");
        if (warehouse.searchClient(clientId) != null) {
            Session session = new Session(Enums.State.CLIENT, clientId);
            context.addSession(session);
            return true;
        } else {
            System.out.println("Client not found."); return false;
        }
    }

    public void terminate(Enums.Transition exitcode) {
        System.out.println("Hello");
        switch(exitcode) {
            case TO_CLIENT:
                context.changeState(Enums.Transition.TO_CLIENT);
                break;
            case CLEAN_EXIT:
                context.handleLogout();
                break;
            default:
                context.changeState(Enums.Transition.ERROR_EXIT);
                break;
        }
    }

    public void process() {
        Enums.Transition exitcode = Enums.Transition.CLEAN_EXIT;
        help();
        boolean done = false;
        while(!done) {
            switch(getCommand()) {
                case ADD_CLIENT:
                    addClient();
                    break;
                case DISPLAY_PRODUCTS:
                    getProducts();
                    break;
                case DISPLAY_CLIENTS:
                    getClients();
                    break;
                case SHOW_CLIENTS_WITH_BALANCE:
                    getClientsWithBalance();
                    break;
                case RECORD_PAYMENT:
                    receivePayment();
                    break;
                case BECOME_CLIENT:
                    if (becomeClient()) {
                        exitcode = Enums.Transition.TO_CLIENT;
                        done = true;
                    }
                    break;
                case HELP:
                    help();
                    break;
                case LOGOUT:
                    exitcode = Enums.Transition.CLEAN_EXIT;
                    done = true;
                    break;
                default:
                    exitcode = Enums.Transition.ERROR_EXIT;
                    done = true;
                    break;
            }
        }

        terminate(exitcode);
    }

    public void run() {
        if (context == null) context = ContextManager.instance();
        process();
    }
}
