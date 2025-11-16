package WarehouseFSM;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ClerkMenuPanel extends JPanel {
    private MainGUI mainGUI;

    public ClerkMenuPanel(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
        setLayout(new GridLayout(0, 1, 10, 10));

        JLabel title = new JLabel("Clerk Menu", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title);

        JButton addClientBtn = new JButton("Add Client");
        JButton displayProductsBtn = new JButton("Display Products");
        JButton displayClientsBtn = new JButton("Display Clients");
        JButton clientsWithBalanceBtn = new JButton("Clients With Balance");
        JButton recordPaymentBtn = new JButton("Record Payment");
        JButton becomeClientBtn = new JButton("Switch to Client Menu");
        JButton logoutBtn = new JButton("Logout");

        add(addClientBtn);
        add(displayProductsBtn);
        add(displayClientsBtn);
        add(clientsWithBalanceBtn);
        add(recordPaymentBtn);
        add(becomeClientBtn);
        add(logoutBtn);

        addClientBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter client name:");
            String address = JOptionPane.showInputDialog(this, "Enter client address:");
            Warehouse.instance().addClient(name, address);
        });

        displayProductsBtn.addActionListener(e -> {
            List<Product> products = Warehouse.instance().getProductsList();
            JTextArea area = new JTextArea();
            products.forEach(p -> area.append(p.toString() + "\n"));
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Products",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        displayClientsBtn.addActionListener(e -> {
            List<Client> clients = Warehouse.instance().getClientsList();
            JTextArea area = new JTextArea();
            clients.forEach(c -> area.append(c.toString() + "\n"));
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Clients",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        clientsWithBalanceBtn.addActionListener(e -> {
            List<Client> clients = Warehouse.instance().getClientsWithBalanceList();
            JTextArea area = new JTextArea();
            clients.forEach(c -> area.append(c.toString() + "\n"));
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Clients With Balance",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        recordPaymentBtn.addActionListener(e -> {
            String clientId = JOptionPane.showInputDialog(this, "Enter client ID:");
            double amount = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter payment amount:"));
            Warehouse.instance().receivePayment(clientId, amount);
        });

        becomeClientBtn.addActionListener(e -> {
            String clientId = JOptionPane.showInputDialog(this, "Enter client ID to switch to:");
            Client client = Warehouse.instance().searchClient(clientId);
            if (client != null) {
                mainGUI.showPanel(new ClientMenuPanel(mainGUI, client));
            }
        });

        logoutBtn.addActionListener(e -> mainGUI.showPanel(new LoginPanel(mainGUI)));
    }
}
