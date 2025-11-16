package WarehouseFSM;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;

public class WishlistPanel extends JPanel {
    private MainGUI mainGUI;
    private Client client;
    private Warehouse warehouse;

    public WishlistPanel(MainGUI mainGUI, Client client) {
        this.mainGUI = mainGUI;
        this.client = client;
        this.warehouse = Warehouse.instance();

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Wishlist for " + client.getName());
        add(title, BorderLayout.NORTH);

        JTextArea wishlistArea = new JTextArea(15, 40);
        wishlistArea.setEditable(false);
        updateWishlistText(wishlistArea);
        add(new JScrollPane(wishlistArea), BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Client Menu");
        backButton.addActionListener(e -> mainGUI.showPanel(new ClientMenuPanel(mainGUI, client)));
        add(backButton, BorderLayout.SOUTH);
    }

    private void updateWishlistText(JTextArea textArea) {
        StringBuilder sb = new StringBuilder();
        for (WishlistItem item : client.getWishlist().getItemsList()) {
            sb.append(item).append("\n");
        }
        textArea.setText(sb.toString());
    }
}
