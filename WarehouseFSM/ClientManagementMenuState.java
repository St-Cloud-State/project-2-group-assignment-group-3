import javax.swing.*;

import java.awt.*;
import java.util.Iterator;

public class ClientManagementMenuState extends State {
    private static ClientManagementMenuState instance;

    private static Warehouse warehouse;

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JLabel breadcrumbLabel;

    private static final String CARD_ADD_CLIENT        = "ADD_CLIENT";
    private static final String CARD_CLIENTS           = "CLIENTS";
    private static final String CARD_CLIENTS_BALANCE   = "CLIENTS_BALANCE";
    private static final String CARD_RECORD_PAYMENT    = "RECORD_PAYMENT";

    private ClientManagementMenuState() {
        warehouse = Warehouse.instance();
    }

    public static ClientManagementMenuState instance() {
        if (instance == null)
            instance = new ClientManagementMenuState();
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

        showView("Add Client", CARD_ADD_CLIENT);

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

        JLabel sectionLabel = new JLabel("Client Management");
        sectionLabel.setForeground(new Color(180, 180, 180));
        sectionLabel.setFont(sectionLabel.getFont().deriveFont(13f));

        header.add(appLabel);
        header.add(Box.createVerticalStrut(4));
        header.add(sectionLabel);

        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setOpaque(false);
        menu.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        menu.add(createSidebarButton("Add Client",
                () -> showView("Add Client", CARD_ADD_CLIENT)));
        menu.add(createSidebarButton("Clients",
                () -> showView("Clients", CARD_CLIENTS)));
        menu.add(createSidebarButton("Clients with Balance",
                () -> showView("Clients with Balance", CARD_CLIENTS_BALANCE)));
        menu.add(createSidebarButton("Record Payment",
                () -> showView("Record Payment", CARD_RECORD_PAYMENT)));

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e ->
                ContextManager.instance().back()
        );
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(8, 8, 16, 8));
        bottom.add(backBtn, BorderLayout.SOUTH);

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

        contentPanel.add(createAddClientPanel(),      CARD_ADD_CLIENT);
        contentPanel.add(createClientsPanel(false),   CARD_CLIENTS);
        contentPanel.add(createClientsPanel(true),    CARD_CLIENTS_BALANCE);
        contentPanel.add(createRecordPaymentPanel(),  CARD_RECORD_PAYMENT);

        main.add(breadcrumbLabel, BorderLayout.NORTH);
        main.add(contentPanel, BorderLayout.CENTER);

        return main;
    }

    private void showView(String viewName, String cardId) {
        breadcrumbLabel.setText("Client Management  >  " + viewName);
        cardLayout.show(contentPanel, cardId);
    }

    private JPanel createAddClientPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = baseGbc();

        JLabel title = titleLabel("Add Client");
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridy++; gbc.gridwidth = 1;
        JLabel nameLabel = fieldLabel("Name:");
        panel.add(nameLabel, gbc);

        JTextField nameField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel addrLabel = fieldLabel("Address:");
        panel.add(addrLabel, gbc);

        JTextField addrField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(addrField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        JButton addBtn = new JButton("Add Client");
        panel.add(addBtn, gbc);

        addBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String addr = addrField.getText().trim();

            if (name.isEmpty() || addr.isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Name and address are required.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Client client = warehouse.addClient(name, addr);
            if (client != null) {
                JOptionPane.showMessageDialog(frame,
                        "Client added! ID: " + client.getId(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                nameField.setText("");
                addrField.setText("");
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Failed to add client.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel createClientsPanel(boolean onlyWithBalance) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = baseGbc();
    
        JLabel title = titleLabel(onlyWithBalance ? "Clients with Balance" : "Clients");
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
    
        String[] columns = {"ID", "Name", "Address", "Balance"};
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
    
            Iterator<Client> it = onlyWithBalance
                    ? warehouse.getClientsWithBalance()
                    : warehouse.getClients();
    
            if (it == null || !it.hasNext()) {
                JOptionPane.showMessageDialog(frame,
                        "No clients found.",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
    
            while (it.hasNext()) {
                Client c = it.next();
                Object[] row = {
                        c.getId(),
                        c.getName(),
                        c.getAddress(),
                        String.format("$%.2f", c.getBalance())
                };
                model.addRow(row);
            }
        });
    
        return panel;
    }

    private JPanel createRecordPaymentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = baseGbc();

        JLabel title = titleLabel("Record Payment");
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridy++; gbc.gridwidth = 1;
        JLabel idLabel = fieldLabel("Client ID:");
        panel.add(idLabel, gbc);

        JTextField idField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel amtLabel = fieldLabel("Payment Amount:");
        panel.add(amtLabel, gbc);

        JTextField amtField = new JTextField(10);
        gbc.gridx = 1;
        panel.add(amtField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        JButton recordBtn = new JButton("Record Payment");
        panel.add(recordBtn, gbc);

        recordBtn.addActionListener(e -> {
            String clientId = idField.getText().trim();
            String amtText  = amtField.getText().trim();

            if (clientId.isEmpty() || amtText.isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "All fields are required.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                float amount = Float.parseFloat(amtText);
                warehouse.receivePayment(clientId, amount);
                JOptionPane.showMessageDialog(frame,
                        "Payment recorded.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Amount must be a number.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
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
}
