import javax.swing.*;
import java.awt.*;

public final class LoginState extends State {
    private static LoginState instance;
    private JFrame frame;
    private final Warehouse warehouse = Warehouse.instance();

    private static final Color BG_MAIN   = new Color(24, 24, 24);
    private static final Color BG_CARD   = new Color(30, 30, 30);
    private static final Color BG_BAR    = new Color(18, 18, 18);
    private static final Color FG_TEXT   = Color.WHITE;
    private static final Color FG_MUTED  = new Color(180, 180, 180);

    private LoginState() {}

    public static LoginState instance() {
        if (instance == null) instance = new LoginState();
        return instance;
    }

    @Override
    public void run() {
        frame = ContextManager.instance().getFrame();

        Container root = frame.getContentPane();
        root.removeAll();
        root.setLayout(new BorderLayout());
        root.setBackground(BG_MAIN);

        // Optional "header" like other screens
        JLabel header = new JLabel("Warehouse  ›  Login");
        header.setForeground(FG_TEXT);
        header.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        header.setFont(header.getFont().deriveFont(Font.BOLD, 14f));
        root.add(header, BorderLayout.NORTH);

        // Tabs container
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setBackground(BG_BAR);
        tabs.setForeground(FG_TEXT);
        tabs.setOpaque(true);

        tabs.addTab("Client", createClientTab());
        tabs.addTab("Clerk", createClerkTab());
        tabs.addTab("Manager", createManagerTab());
        tabs.setSelectedIndex(0);

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(BG_MAIN);
        center.add(tabs, BorderLayout.CENTER);

        JButton exitButton = new JButton("Exit");
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomBar.setBackground(BG_BAR);
        bottomBar.setBorder(BorderFactory.createEmptyBorder(8, 16, 16, 16));
        bottomBar.add(exitButton);

        exitButton.addActionListener(e ->
            ContextManager.instance().changeState(Enums.Transition.CLEAN_EXIT)
        );

        root.add(center, BorderLayout.CENTER);
        root.add(bottomBar, BorderLayout.SOUTH);

        frame.revalidate();
        frame.repaint();
    }

    private JPanel createClientTab() {
        JPanel panel = baseFormPanel();

        GridBagConstraints gbc = baseGbc();

        JLabel title = new JLabel("Client Login");
        styleTitleLabel(title);
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel idLabel = new JLabel("Client ID:");
        styleFieldLabel(idLabel);
        panel.add(idLabel, gbc);

        JTextField clientIdField = new JTextField(18);
        gbc.gridx = 1;
        panel.add(clientIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;

        JButton loginButton = new JButton("Login as Client");
        panel.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String clientId = clientIdField.getText().trim();
            if (clientId.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Please enter a client ID.",
                        "Missing ID",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (warehouse.searchClient(clientId) != null) {
                ContextManager ctx = ContextManager.instance();
                ctx.addSession(new Session(Enums.State.CLIENT, clientId));
                ctx.changeState(Enums.Transition.TO_CLIENT);
            } else {
                JOptionPane.showMessageDialog(
                        frame,
                        "No client found with ID: " + clientId,
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        return panel;
    }

    private JPanel createClerkTab() {
        JPanel panel = baseFormPanel();
        GridBagConstraints gbc = baseGbc();

        JLabel title = new JLabel("Clerk Login");
        styleTitleLabel(title);
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel userLabel = new JLabel("Username:");
        styleFieldLabel(userLabel);
        panel.add(userLabel, gbc);

        JTextField usernameField = new JTextField(18);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel passLabel = new JLabel("Password:");
        styleFieldLabel(passLabel);
        panel.add(passLabel, gbc);

        JPasswordField passwordField = new JPasswordField(18);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;

        JButton loginButton = new JButton("Login as Clerk");
        panel.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if ("clerk".equals(username) && "clerk".equals(password)) {
                ContextManager ctx = ContextManager.instance();
                ctx.addSession(new Session(Enums.State.CLERK, username));
                ctx.changeState(Enums.Transition.TO_CLERK);
            } else {
                JOptionPane.showMessageDialog(
                        frame,
                        "Invalid credentials for Clerk.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        return panel;
    }

    private JPanel createManagerTab() {
        JPanel panel = baseFormPanel();
        GridBagConstraints gbc = baseGbc();

        JLabel title = new JLabel("Manager Login");
        styleTitleLabel(title);
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel userLabel = new JLabel("Username:");
        styleFieldLabel(userLabel);
        panel.add(userLabel, gbc);

        JTextField usernameField = new JTextField(18);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel passLabel = new JLabel("Password:");
        styleFieldLabel(passLabel);
        panel.add(passLabel, gbc);

        JPasswordField passwordField = new JPasswordField(18);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;

        JButton loginButton = new JButton("Login as Manager");
        panel.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if ("manager".equals(username) && "manager".equals(password)) {
                ContextManager ctx = ContextManager.instance();
                ctx.addSession(new Session(Enums.State.MANAGER, username));
                ctx.changeState(Enums.Transition.TO_MANAGER);
            } else {
                JOptionPane.showMessageDialog(
                        frame,
                        "Invalid credentials for Manager.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        return panel;
    }

    /* ---------- small UI helpers ---------- */

    private JPanel baseFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_CARD);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
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

    private void styleTitleLabel(JLabel label) {
        label.setForeground(FG_TEXT);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 20f));
    }

    private void styleFieldLabel(JLabel label) {
        label.setForeground(FG_MUTED);
    }
}
