package WarehouseFSM;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ManagerMenuPanel extends JPanel {
    private MainGUI mainGUI;
    private Warehouse warehouse;

    public ManagerMenuPanel(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
        this.warehouse = Warehouse.instance();

        setLayout(new GridLayout(5, 1, 10, 10));

        JLabel title = new JLabel("Manager Menu", SwingConstants.CENTER);
        add(title);

        JButton viewClientsButton = new JButton("View All Clients");
        viewClientsButton.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (Client c : warehouse.getClientsList()) {
                sb.append(c).append("\n");
            }
            JTextArea area = new JTextArea(sb.toString());
            area.setEditable(false);
            JOptionPane.showMessageDialog(mainGUI, new JScrollPane(area));
        });
        add(viewClientsButton);

        JButton viewProductsButton = new JButton("View Products");
        viewProductsButton.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (Product p : warehouse.getProductsList()) {
                sb.append(p).append("\n");
            }
            JTextArea area = new JTextArea(sb.toString());
            area.setEditable(false);
            JOptionPane.showMessageDialog(mainGUI, new JScrollPane(area));
        });
        add(viewProductsButton);

        JButton clearWishlistButton = new JButton("Clear All Wishlists");
        clearWishlistButton.addActionListener(e -> {
            for (Client client : warehouse.getClientsList()) {
                client.getWishlist().clear();
            }
            JOptionPane.showMessageDialog(mainGUI, "All wishlists cleared.");
        });
        add(clearWishlistButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> mainGUI.showPanel(new LoginPanel(mainGUI)));
        add(logoutButton);
    }
}
