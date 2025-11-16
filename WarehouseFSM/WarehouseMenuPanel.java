package WarehouseFSM;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.ArrayList;

public class WarehouseMenuPanel extends JPanel {
    private MainGUI mainGUI;
    private Warehouse warehouse;

    public WarehouseMenuPanel(MainGUI gui) {
        this.mainGUI = gui;
        this.warehouse = Warehouse.instance();

        setLayout(new GridLayout(0, 1, 10, 10));

        JButton addClientBtn = new JButton("Add Client");
        JButton addProductBtn = new JButton("Add Product");
        JButton viewClientsBtn = new JButton("View Clients");
        JButton viewProductsBtn = new JButton("View Products");

        addClientBtn.addActionListener(e -> addClient());
        addProductBtn.addActionListener(e -> addProduct());
        viewClientsBtn.addActionListener(e -> viewClients());
        viewProductsBtn.addActionListener(e -> viewProducts());

        add(addClientBtn);
        add(addProductBtn);
        add(viewClientsBtn);
        add(viewProductsBtn);
    }

    private void addClient() {
        String name = JOptionPane.showInputDialog(this, "Enter client name:");
        String address = JOptionPane.showInputDialog(this, "Enter client address:");
        Client client = warehouse.addClient(name, address);
        if (client != null) JOptionPane.showMessageDialog(this, "Client added!");
        else JOptionPane.showMessageDialog(this, "Failed to add client.");
    }

    private void addProduct() {
        String name = JOptionPane.showInputDialog(this, "Enter product name:");
        float price = Float.parseFloat(JOptionPane.showInputDialog(this, "Enter sale price:"));
        int quantity = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter quantity:"));
        Product p = warehouse.addProduct(name, price, quantity);
        if (p != null) JOptionPane.showMessageDialog(this, "Product added!");
        else JOptionPane.showMessageDialog(this, "Failed to add product.");
    }

    private void viewClients() {
        StringBuilder sb = new StringBuilder();
        for (Iterator<Client> it = warehouse.getClients(); it.hasNext();) {
            sb.append(it.next().toString()).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.length() > 0 ? sb.toString() : "No clients.");
    }

    private void viewProducts() {
        StringBuilder sb = new StringBuilder();
        for (Iterator<Product> it = warehouse.getProducts(); it.hasNext();) {
            sb.append(it.next().toString()).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.length() > 0 ? sb.toString() : "No products.");
    }
}
