import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class ClientMenuState extends State {
    private static ClientMenuState instance;

    private static Warehouse warehouse;
    private Client currentClient;

    private static final String CARD_DETAILS      = "DETAILS";
    private static final String CARD_PRODUCTS     = "PRODUCTS";
    private static final String CARD_WISHLIST     = "WISHLIST";
    private static final String CARD_ORDER        = "ORDER";
    private static final String CARD_TRANSACTIONS = "TRANSACTIONS";

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JLabel breadcrumbLabel;
    private JLabel balValueLabel;

    private ClientMenuState() {
        warehouse = Warehouse.instance();
    }

    public static ClientMenuState instance() {
        if (instance == null) {
            instance = new ClientMenuState();
        }
        return instance;
    }

    @Override
    public void run() {
        ContextManager ctx = ContextManager.instance();
        String currentClientId = ctx.getSession().userId();
        currentClient = warehouse.searchClient(currentClientId);

        frame = ctx.getFrame();
        Container root = frame.getContentPane();
        root.removeAll();
        root.setLayout(new BorderLayout());

        JPanel sidebar  = createSidebar();
        JPanel mainArea = createMainArea();

        root.add(sidebar, BorderLayout.WEST);
        root.add(mainArea, BorderLayout.CENTER);

        showView("My Details", CARD_DETAILS);

        frame.revalidate();
        frame.repaint();
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBackground(new Color(18, 18, 18));

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel appLabel = new JLabel("Warehouse");
        appLabel.setForeground(Color.WHITE);
        appLabel.setFont(appLabel.getFont().deriveFont(Font.BOLD, 18f));

        JLabel sectionLabel = new JLabel("Client");
        sectionLabel.setForeground(new Color(180, 180, 180));
        sectionLabel.setFont(sectionLabel.getFont().deriveFont(13f));

        header.add(appLabel);
        header.add(Box.createVerticalStrut(4));
        header.add(sectionLabel);

        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setOpaque(false);
        menu.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        menu.add(createSidebarButton("My Details",
                () -> showView("My Details", CARD_DETAILS)));
        menu.add(createSidebarButton("Products",
                () -> showView("Products", CARD_PRODUCTS)));
        menu.add(createSidebarButton("Wishlist",
                () -> showView("Wishlist", CARD_WISHLIST)));
        menu.add(createSidebarButton("Place Order",
                () -> showView("Place Order", CARD_ORDER)));
        menu.add(createSidebarButton("Transactions",
                () -> showView("Transactions", CARD_TRANSACTIONS)));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e ->
                ContextManager.instance().handleLogout()
        );
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(8, 8, 16, 8));
        bottom.add(logoutBtn, BorderLayout.SOUTH);

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
        contentPanel.setBackground(new Color(30, 30, 30));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        contentPanel.add(createDetailsPanel(),      CARD_DETAILS);
        contentPanel.add(createProductsPanel(),     CARD_PRODUCTS);
        contentPanel.add(createTransactionsPanel(), CARD_TRANSACTIONS);
        contentPanel.add(createOrderPanel(),        CARD_ORDER);
        contentPanel.add(createWishlistPanel(),     CARD_WISHLIST);

        main.add(breadcrumbLabel, BorderLayout.NORTH);
        main.add(contentPanel, BorderLayout.CENTER);

        return main;
    }

    private void showView(String viewName, String cardId) {
        if (CARD_DETAILS.equals(cardId)) {
            refreshClientDetails();
        }
        breadcrumbLabel.setText("Client  >  " + viewName);
        cardLayout.show(contentPanel, cardId);
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = baseGbc();

        JLabel title = titleLabel("My Details");
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridy++; gbc.gridwidth = 1;
        JLabel idLabel = fieldLabel("ID:");
        panel.add(idLabel, gbc);

        JLabel idValue = fieldLabel(currentClient.getId());
        gbc.gridx = 1;
        panel.add(idValue, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel nameLabel = fieldLabel("Name:");
        panel.add(nameLabel, gbc);

        JLabel nameValue = fieldLabel(currentClient.getName());
        gbc.gridx = 1;
        panel.add(nameValue, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel addrLabel = fieldLabel("Address:");
        panel.add(addrLabel, gbc);

        JLabel addrValue = fieldLabel(currentClient.getAddress());
        gbc.gridx = 1;
        panel.add(addrValue, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel balLabel = fieldLabel("Balance:");
        panel.add(balLabel, gbc);

        balValueLabel = fieldLabel(String.format("$%.2f", currentClient.getBalance()));
        gbc.gridx = 1;
        panel.add(balValueLabel, gbc);

        return panel;
    }

    private JPanel createProductsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = baseGbc();
    
        JLabel title = titleLabel("Products");
        gbc.gridwidth = 2;
        panel.add(title, gbc);
    
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        JButton refreshBtn = new JButton("Refresh");
        panel.add(refreshBtn, gbc);
    
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
    
        String[] columns = {"ID", "Name", "Unit Price", "Quantity"};
        javax.swing.table.DefaultTableModel model =
                new javax.swing.table.DefaultTableModel(columns, 0) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
    
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
    
        table.setBackground(new Color(30, 30, 30));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(60, 60, 60));
        table.setSelectionBackground(new Color(55, 55, 55));
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setBackground(new Color(20, 20, 20));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        table.setCellSelectionEnabled(true);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(true);
        table.setDragEnabled(true);
    
        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, gbc);
    
        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
    
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
                Object[] row = {
                        p.getId(),
                        p.getName(),
                        String.format("$%.2f", p.getSalePrice()),
                        p.getAmount()
                };
                model.addRow(row);
            }
        });
    
        return panel;
    }    

    private JPanel createTransactionsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = baseGbc();
    
        JLabel title = titleLabel("Transaction History");
        gbc.gridwidth = 2;
        panel.add(title, gbc);
    
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        JButton refreshBtn = new JButton("Refresh");
        panel.add(refreshBtn, gbc);
    
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.6;
        gbc.fill = GridBagConstraints.BOTH;
    
        String[] columns = {"Invoice ID", "Date", "Amount"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
    
        table.setBackground(new Color(30, 30, 30));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(60, 60, 60));
        table.setSelectionBackground(new Color(55, 55, 55));
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setBackground(new Color(20, 20, 20));
        table.getTableHeader().setForeground(Color.WHITE);
    
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDragEnabled(true);
    
        JScrollPane tableScroll = new JScrollPane(table);
        panel.add(tableScroll, gbc);
    
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        JLabel detailLabel = fieldLabel("Invoice Details");
        panel.add(detailLabel, gbc);
    
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.4;
        gbc.fill = GridBagConstraints.BOTH;
    
        JTextArea detailArea = new JTextArea(10, 40);
        detailArea.setEditable(false);
        detailArea.setBackground(new Color(20, 20, 20));
        detailArea.setForeground(Color.WHITE);
        detailArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
    
        JScrollPane detailScroll = new JScrollPane(detailArea);
        panel.add(detailScroll, gbc);
    
        java.util.List<Invoice> invoiceRows = new ArrayList<>();
    
        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
            invoiceRows.clear();
            detailArea.setText("");
    
            InvoiceList invoices = currentClient.getInvoices();
            if (invoices == null || invoices.getItems() == null || !invoices.getItems().hasNext()) {
                detailArea.setText("No transactions found.");
                return;
            }
    
            Iterator<Invoice> it = invoices.getItems();
            while (it.hasNext()) {
                Invoice inv = it.next();
                invoiceRows.add(inv);
    
                Object[] row = {
                        inv.getId(),
                        inv.getDate(),                          
                        String.format("$%.2f", inv.balanceDue())
                };
                model.addRow(row);
            }
        });
    
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
    
            int row = table.getSelectedRow();
            if (row < 0 || row >= invoiceRows.size()) {
                detailArea.setText("");
                return;
            }
    
            Invoice selected = invoiceRows.get(row);
            detailArea.setText(selected.render());
            detailArea.setCaretPosition(0);
        });
    
        return panel;
    }    

    private JPanel createOrderPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = baseGbc();
    
        JLabel title = titleLabel("Place Order from Wishlist");
        gbc.gridwidth = 2;
        panel.add(title, gbc);
    
        gbc.gridy++;
        gbc.gridwidth = 1;
        JButton loadWishlistBtn = new JButton("Load Wishlist");
        panel.add(loadWishlistBtn, gbc);
    
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
    
        String[] wishlistCols = {"Product ID", "Product Name", "Quantity"};
        DefaultTableModel wishlistModel = new DefaultTableModel(wishlistCols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable wishlistTable = new JTable(wishlistModel);
        wishlistTable.setFillsViewportHeight(true);
    
        String[] orderCols = {"Product ID", "Product Name", "Quantity"};
        DefaultTableModel orderModel = new DefaultTableModel(orderCols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        ArrayList<OrderItem> orders = new ArrayList<OrderItem>();

        JTable orderTable = new JTable(orderModel);
        orderTable.setFillsViewportHeight(true);
    
        for (JTable t : new JTable[]{wishlistTable, orderTable}) {
            t.setBackground(new Color(30, 30, 30));
            t.setForeground(Color.WHITE);
            t.setGridColor(new Color(60, 60, 60));
            t.setSelectionBackground(new Color(55, 55, 55));
            t.setSelectionForeground(Color.WHITE);
            t.getTableHeader().setBackground(new Color(20, 20, 20));
            t.getTableHeader().setForeground(Color.WHITE);
            t.setCellSelectionEnabled(true);
            t.setRowSelectionAllowed(true);
            t.setColumnSelectionAllowed(true);
            t.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            t.setDragEnabled(true);
        }
    
        JPanel tablesContainer = new JPanel(new GridLayout(1, 2, 16, 0));
        tablesContainer.setOpaque(false);
    
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        JLabel wishlistLabel = fieldLabel("Wishlist");
        wishlistLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        leftPanel.add(wishlistLabel, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(wishlistTable), BorderLayout.CENTER);
    
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        JLabel orderLabel = fieldLabel("Currently in Your Order");
        orderLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        rightPanel.add(orderLabel, BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(orderTable), BorderLayout.CENTER);
    
        tablesContainer.add(leftPanel);
        tablesContainer.add(rightPanel);
    
        panel.add(tablesContainer, gbc);
    
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        JLabel idLabel = fieldLabel("Product ID from wishlist:");
        panel.add(idLabel, gbc);
    
        JTextField idField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(idField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel qtyLabel = fieldLabel("Quantity to purchase:");
        panel.add(qtyLabel, gbc);
    
        JTextField qtyField = new JTextField(10);
        gbc.gridx = 1;
        panel.add(qtyField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
    
        JButton addToInvoiceBtn = new JButton("Add to Order");
        panel.add(addToInvoiceBtn, gbc);
    
        gbc.gridy++;
        JButton finalizeBtn = new JButton("Finalize Order");
        panel.add(finalizeBtn, gbc);
    
        wishlistTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = wishlistTable.getSelectedRow();
                if (row >= 0) {
                    Object val = wishlistModel.getValueAt(row, 0);
                    idField.setText(val == null ? "" : val.toString());
                }
            }
        });
    
        loadWishlistBtn.addActionListener(e -> {
            loadWishlistIntoTable(wishlistModel);
        });
    
        addToInvoiceBtn.addActionListener(e -> {
            String prodId = idField.getText().trim();
            String qtyText = qtyField.getText().trim();
    
            if (prodId.isEmpty() || qtyText.isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Product ID and quantity are required.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
    
            Wishlist wishlist = currentClient.getWishlist();
            if (wishlist == null || wishlist.getItems() == null || !wishlistContainsProduct(wishlist, prodId)) {
                JOptionPane.showMessageDialog(frame,
                        "Product is not in your wishlist.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            try {
                int qty = Integer.parseInt(qtyText);
                if (qty <= 0) {
                    JOptionPane.showMessageDialog(frame,
                            "Quantity must be greater than zero.",
                            "Validation",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                orders.add(new OrderItem(prodId, qty));
    
                removeFromWishlist(wishlist, prodId);
                loadWishlistIntoTable(wishlistModel);
    
                Product p = warehouse.searchProduct(prodId);
                String name = (p != null ? p.getName() : "(Unknown Product)");
                orderModel.addRow(new Object[]{prodId, name, qty});
    
                JOptionPane.showMessageDialog(frame,
                        "Item added to order.",
                        "Success",
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
    
        finalizeBtn.addActionListener(e -> {
            if (orders.isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "No items in the order.",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
    
            int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to make this purchase?",
                    "Confirm Purchase",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
    
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
    
            Invoice invoice = new Invoice(currentClient.getId());
            for (OrderItem item : orders) {
                InvoiceItem invoiceItem =
                        warehouse.order(item.getProductId(), item.getQuantity(), currentClient.getId());
                invoice.addItem(invoiceItem);
            }

            currentClient.getInvoices().insertItem(invoice);
    
            JOptionPane.showMessageDialog(frame,
                    invoice.render(),
                    "Order Summary",
                    JOptionPane.INFORMATION_MESSAGE);
    
            orders.clear();
            orderModel.setRowCount(0);
        });
    
        return panel;
    }    

    private JPanel createWishlistPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = baseGbc();
    
        JLabel title = titleLabel("Wishlist");
        gbc.gridwidth = 2;
        panel.add(title, gbc);
    
        gbc.gridy++; gbc.gridwidth = 1;
        JLabel prodLabel = fieldLabel("Product ID:");
        panel.add(prodLabel, gbc);
    
        JTextField productIdField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(productIdField, gbc);
    
        gbc.gridx = 0; gbc.gridy++;
        JLabel qtyLabel = fieldLabel("Quantity:");
        panel.add(qtyLabel, gbc);
    
        JTextField qtyField = new JTextField(10);
        gbc.gridx = 1;
        panel.add(qtyField, gbc);
    
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        JButton addBtn = new JButton("Add to Wishlist");
        panel.add(addBtn, gbc);
    
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        JButton refreshBtn = new JButton("Refresh Wishlist");
        panel.add(refreshBtn, gbc);
    
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
    
        String[] columns = {"Product ID", "Product Name", "Quantity"};
        javax.swing.table.DefaultTableModel model =
                new javax.swing.table.DefaultTableModel(columns, 0) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
    
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
    
        table.setBackground(new Color(30, 30, 30));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(60, 60, 60));
        table.setSelectionBackground(new Color(55, 55, 55));
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setBackground(new Color(20, 20, 20));
        table.getTableHeader().setForeground(Color.WHITE);
    
        table.setCellSelectionEnabled(true);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        table.setDragEnabled(true);
    
        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, gbc);
    
        addBtn.addActionListener(e -> {
            String productId = productIdField.getText().trim();
            String qtyText   = qtyField.getText().trim();
    
            if (productId.isEmpty() || qtyText.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Product ID and quantity are required.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
    
            int quantity;
            try {
                quantity = Integer.parseInt(qtyText);
                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Quantity must be greater than zero.",
                            "Validation",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Quantity must be a number.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
    
            Map<String, Object> response =
                    warehouse.addToWishlist(productId, quantity, currentClient.getId());
    
            if ("success".equals(response.get("status"))) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Added to wishlist successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                productIdField.setText("");
                qtyField.setText("");
    
                loadWishlistIntoTable(model);
            } else {
                JOptionPane.showMessageDialog(
                        frame,
                        "Failed to add: " + response.get("message"),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        refreshBtn.addActionListener(e -> loadWishlistIntoTable(model));
    
        return panel;
    }

    private void loadWishlistIntoTable(javax.swing.table.DefaultTableModel model) {
        model.setRowCount(0);
    
        Wishlist wishlist = currentClient.getWishlist();
        if (wishlist == null || wishlist.getItems() == null || !wishlist.getItems().hasNext()) {
            return;
        }
    
        Iterator<WishlistItem> it = wishlist.getItems();
        while (it.hasNext()) {
            WishlistItem item = it.next();
            String productId = item.getProductId();
    
            Product p = warehouse.searchProduct(productId);
            String name = (p != null) ? p.getName() : "(Unknown Product)";
    
            int qty = item.getQuantity();
    
            model.addRow(new Object[] { productId, name, qty });
        }
    }
        
    private boolean wishlistContainsProduct(Wishlist wishlist, String productId) {
        Iterator<WishlistItem> it = wishlist.getItems();
        while (it.hasNext()) {
            if (it.next().getProductId().equals(productId)) return true;
        }
        return false;
    }

    private void removeFromWishlist(Wishlist wishlist, String productId) {
        Iterator<WishlistItem> it = wishlist.getItems();
        while (it.hasNext()) {
            WishlistItem item = it.next();
            if (item.getProductId().equals(productId)) {
                it.remove();
                break;
            }
        }
    }

    private GridBagConstraints baseGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        return gbc;
    }

    private JLabel titleLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 20f));
        return lbl;
    }

    private JLabel fieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        return lbl;
    }

    private void refreshClientDetails() {
        currentClient = warehouse.searchClient(currentClient.getId());
        balValueLabel.setText(String.format("$%.2f", currentClient.getBalance()));
    }
}
