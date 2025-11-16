import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public final class ManagerMenuState extends State {
    private static ManagerMenuState instance;

    private static Warehouse warehouse;

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JLabel breadcrumbLabel;

    private static final String CARD_ADD_PRODUCT   = "ADD_PRODUCT";
    private static final String CARD_WAITLIST      = "WAITLIST";
    private static final String CARD_SHIPMENT      = "SHIPMENT";
    private static final String CARD_BECOME_CLERK  = "BECOME_CLERK";

    private ManagerMenuState() {
        warehouse = Warehouse.instance();
    }

    public static ManagerMenuState instance() {
        if (instance == null)
            instance = new ManagerMenuState();
        return instance;
    }

    @Override
    public void run() {
        frame = ContextManager.instance().getFrame();
        Container root = frame.getContentPane();
        root.removeAll();
        root.setLayout(new BorderLayout());

        JPanel sidebar = createSidebar();
        JPanel mainArea = createMainArea();

        root.add(sidebar, BorderLayout.WEST);
        root.add(mainArea, BorderLayout.CENTER);

        showView("Add Product", CARD_ADD_PRODUCT);

        frame.revalidate();
        frame.repaint();
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBackground(new Color(18, 18, 18)); // dark-ish

        // top "Warehouse / Manager"
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel appLabel = new JLabel("Warehouse");
        appLabel.setForeground(Color.WHITE);
        appLabel.setFont(appLabel.getFont().deriveFont(Font.BOLD, 18f));

        JLabel sectionLabel = new JLabel("Manager");
        sectionLabel.setForeground(new Color(180, 180, 180));
        sectionLabel.setFont(sectionLabel.getFont().deriveFont(13f));

        header.add(appLabel);
        header.add(Box.createVerticalStrut(4));
        header.add(sectionLabel);

        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setOpaque(false);
        menu.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        menu.add(createSidebarButton("Add Product", () ->
                showView("Add Product", CARD_ADD_PRODUCT)
        ));
        menu.add(createSidebarButton("Display Waitlist", () ->
                showView("Display Waitlist", CARD_WAITLIST)
        ));
        menu.add(createSidebarButton("Receive Shipment", () ->
                showView("Receive Shipment", CARD_SHIPMENT)
        ));
        menu.add(createSidebarButton("Become Clerk", () ->
                showView("Become Clerk", CARD_BECOME_CLERK)
        ));

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> ContextManager.instance().handleLogout());
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(8, 8, 16, 8));
        bottom.add(logoutButton, BorderLayout.SOUTH);

        sidebar.add(header, BorderLayout.NORTH);
        sidebar.add(menu, BorderLayout.CENTER);
        sidebar.add(bottom, BorderLayout.SOUTH);

        return sidebar;
    }

    private JButton createSidebarButton(String text, Runnable onClick) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.addActionListener(e -> onClick.run());
        return btn;
    }

    private JPanel createMainArea() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(24, 24, 24));

        breadcrumbLabel = new JLabel();
        breadcrumbLabel.setForeground(Color.WHITE);
        breadcrumbLabel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        breadcrumbLabel.setFont(breadcrumbLabel.getFont().deriveFont(14f));

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        contentPanel.setBackground(new Color(30, 30, 30));

        contentPanel.add(createAddProductPanel(),   CARD_ADD_PRODUCT);
        contentPanel.add(createWaitlistPanel(),     CARD_WAITLIST);
        contentPanel.add(createShipmentPanel(),     CARD_SHIPMENT);
        contentPanel.add(createBecomeClerkPanel(),  CARD_BECOME_CLERK);

        main.add(breadcrumbLabel, BorderLayout.NORTH);
        main.add(contentPanel, BorderLayout.CENTER);

        return main;
    }

    private void showView(String viewName, String cardId) {
        breadcrumbLabel.setText("Manager  >  " + viewName);
        cardLayout.show(contentPanel, cardId);
    }

    private JPanel createAddProductPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel title = new JLabel("Add Product");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridy++; gbc.gridwidth = 1;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);
        panel.add(nameLabel, gbc);

        JTextField nameField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel priceLabel = new JLabel("Unit Price:");
        priceLabel.setForeground(Color.WHITE);
        panel.add(priceLabel, gbc);

        JTextField priceField = new JTextField(10);
        gbc.gridx = 1;
        panel.add(priceField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel qtyLabel = new JLabel("Initial Quantity:");
        qtyLabel.setForeground(Color.WHITE);
        panel.add(qtyLabel, gbc);

        JTextField qtyField = new JTextField(10);
        gbc.gridx = 1;
        panel.add(qtyField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        JButton addBtn = new JButton("Add Product");
        panel.add(addBtn, gbc);

        addBtn.addActionListener(e -> {
            if (!confirmManagerPassword()) {
                return;
            }

            String name = nameField.getText().trim();
            String priceText = priceField.getText().trim();
            String qtyText = qtyField.getText().trim();

            if (name.isEmpty() || priceText.isEmpty() || qtyText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields are required.",
                        "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                float price = Float.parseFloat(priceText);
                int qty = Integer.parseInt(qtyText);

                Product p = warehouse.addProduct(name, price, qty);
                if (p != null) {
                    JOptionPane.showMessageDialog(frame,
                            "Product added! ID: " + p.getId(),
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    nameField.setText("");
                    priceField.setText("");
                    qtyField.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Failed to add product.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Price and quantity must be valid numbers.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel createWaitlistPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel title = new JLabel("Display Waitlists");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        JButton loadBtn = new JButton("Load Products with Waitlists");
        panel.add(loadBtn, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;

        String[] productCols = {"Product ID", "Product Name"};
        DefaultTableModel productModel = new DefaultTableModel(productCols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable productTable = new JTable(productModel);
        productTable.setFillsViewportHeight(true);

        productTable.setBackground(new Color(30, 30, 30));
        productTable.setForeground(Color.WHITE);
        productTable.setGridColor(new Color(60, 60, 60));
        productTable.setSelectionBackground(new Color(55, 55, 55));
        productTable.setSelectionForeground(Color.WHITE);
        productTable.getTableHeader().setBackground(new Color(20, 20, 20));
        productTable.getTableHeader().setForeground(Color.WHITE);
        productTable.setRowSelectionAllowed(true);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.setDragEnabled(true);

        JScrollPane productScroll = new JScrollPane(productTable);
        panel.add(productScroll, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel detailLabel = new JLabel("Waitlist Details (Client ID, Quantity)");
        detailLabel.setForeground(Color.WHITE);
        panel.add(detailLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;

        String[] waitlistCols = {"Client ID", "Quantity"};
        DefaultTableModel waitlistModel = new DefaultTableModel(waitlistCols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable waitlistTable = new JTable(waitlistModel);
        waitlistTable.setFillsViewportHeight(true);

        waitlistTable.setBackground(new Color(30, 30, 30));
        waitlistTable.setForeground(Color.WHITE);
        waitlistTable.setGridColor(new Color(60, 60, 60));
        waitlistTable.setSelectionBackground(new Color(55, 55, 55));
        waitlistTable.setSelectionForeground(Color.WHITE);
        waitlistTable.getTableHeader().setBackground(new Color(20, 20, 20));
        waitlistTable.getTableHeader().setForeground(Color.WHITE);
        waitlistTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        waitlistTable.setCellSelectionEnabled(true);
        waitlistTable.setRowSelectionAllowed(true);
        waitlistTable.setColumnSelectionAllowed(true);
        waitlistTable.setDragEnabled(true);

        JScrollPane waitlistScroll = new JScrollPane(waitlistTable);
        panel.add(waitlistScroll, gbc);

        java.util.List<Product> productsWithWaitlist = new ArrayList<>();

        loadBtn.addActionListener(e -> {
            if (!confirmManagerPassword()) {
                return;
            }

            productModel.setRowCount(0);
            waitlistModel.setRowCount(0);
            productsWithWaitlist.clear();

            Iterator<Product> it = warehouse.getProducts();
            if (it == null || !it.hasNext()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "No products found.",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            while (it.hasNext()) {
                Product p = it.next();
                Waitlist wl = p.getWaitlist();
                if (wl == null) continue;

                Iterator<WaitlistItem> wit = wl.getItems();
                if (wit == null || !wit.hasNext()) continue;

                productsWithWaitlist.add(p);
                productModel.addRow(new Object[]{p.getId(), p.getName()});
            }

            if (productsWithWaitlist.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "No products currently have a waitlist.",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        productTable.getSelectionModel().addListSelectionListener(ev -> {
            if (ev.getValueIsAdjusting()) return;

            int row = productTable.getSelectedRow();
            waitlistModel.setRowCount(0);

            if (row < 0 || row >= productsWithWaitlist.size()) {
                return;
            }

            Product p = productsWithWaitlist.get(row);
            Waitlist wl = p.getWaitlist();
            if (wl == null) {
                return;
            }

            Iterator<WaitlistItem> it = wl.getItems();
            if (it == null || !it.hasNext()) {
                return;
            }

            while (it.hasNext()) {
                WaitlistItem item = it.next();

                String clientId = item.getClientId();
                int quantity = item.getQuantity();
                waitlistModel.addRow(new Object[]{clientId, quantity});
            }
        });

        return panel;
    }


    private JPanel createShipmentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel title = new JLabel("Receive Shipment");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridy++; gbc.gridwidth = 1;
        JLabel idLabel = new JLabel("Product ID:");
        idLabel.setForeground(Color.WHITE);
        panel.add(idLabel, gbc);

        JTextField idField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel qtyLabel = new JLabel("Quantity Received:");
        qtyLabel.setForeground(Color.WHITE);
        panel.add(qtyLabel, gbc);

        JTextField qtyField = new JTextField(10);
        gbc.gridx = 1;
        panel.add(qtyField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        JButton receiveBtn = new JButton("Apply Shipment");
        panel.add(receiveBtn, gbc);

        receiveBtn.addActionListener(e -> {
            if (!confirmManagerPassword()) {
                return;
            }

            String id = idField.getText().trim();
            String qtyText = qtyField.getText().trim();

            if (id.isEmpty() || qtyText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields are required.",
                        "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int qty = Integer.parseInt(qtyText);
                String result = warehouse.receiveShipment(id, qty);
                JOptionPane.showMessageDialog(frame,
                        result,
                        "Shipment Result",
                        JOptionPane.INFORMATION_MESSAGE);
                idField.setText("");
                qtyField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Quantity must be a number.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel createBecomeClerkPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel title = new JLabel("Become Clerk");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        panel.add(title, gbc);

        gbc.gridy++;
        JLabel info = new JLabel("Switch to the clerk view to perform clerk operations.");
        info.setForeground(Color.WHITE);
        panel.add(info, gbc);

        gbc.gridy++;
        JButton switchBtn = new JButton("Switch to Clerk");
        panel.add(switchBtn, gbc);

        switchBtn.addActionListener(e -> {
            if (!confirmManagerPassword()) {
                return;
            }

            Session session = new Session(Enums.State.CLERK, "clerk");
            ContextManager ctx = ContextManager.instance();
            ctx.addSession(session);
            ctx.changeState(Enums.Transition.TO_CLERK);
        });

        return panel;
    }

    private boolean confirmManagerPassword() {
        JPasswordField passwordField = new JPasswordField(15);
    
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
    
        panel.add(new JLabel("Enter manager password:"), gbc);
        gbc.gridy++;
        panel.add(passwordField, gbc);
    
        int result = JOptionPane.showConfirmDialog(
                frame,
                panel,
                "Manager Confirmation",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
    
        if (result != JOptionPane.OK_OPTION) {
            return false;
        }
    
        String password = new String(passwordField.getPassword()).trim();
        if (!"manager".equals(password)) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Invalid manager password.",
                    "Access Denied",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    
        return true;
    }    
}
