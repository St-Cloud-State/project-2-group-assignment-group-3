package WarehouseFSM;

import javax.swing.*;
import java.awt.*;

public class ClientMenuPanel extends JPanel {
    private MainGUI mainGUI;
    private Client client;

    public ClientMenuPanel(MainGUI mainGUI, Client client) {
        this.mainGUI = mainGUI;
        this.client = client;

        setLayout(new GridLayout(5, 1, 10, 10));

        JLabel title = new JLabel("Client Menu: " + client.getName(), SwingConstants.CENTER);
        add(title);

        JButton wishlistButton = new JButton("Manage Wishlist");
        wishlistButton.addActionListener(e -> mainGUI.showPanel(new WishlistPanel(mainGUI, client)));
        add(wishlistButton);

        JButton addFundsButton = new JButton("Add Funds");
        addFundsButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(mainGUI, "Enter amount to add:");
            try {
                double amount = Double.parseDouble(input);
                client.addFunds(amount);
                JOptionPane.showMessageDialog(mainGUI, "Added $" + amount + " to balance.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainGUI, "Invalid number.");
            }
        });
        add(addFundsButton);

        JButton viewBalanceButton = new JButton("View Balance");
        viewBalanceButton.addActionListener(e ->
                JOptionPane.showMessageDialog(mainGUI, "Current balance: $" + client.getBalance()));
        add(viewBalanceButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> mainGUI.showPanel(new LoginPanel(mainGUI)));
        add(logoutButton);
    }
}
