package WarehouseFSM;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginPanel extends JPanel {
    private MainGUI mainGUI;

    public LoginPanel(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
        setLayout(new GridLayout(3, 2, 10, 10));

        JLabel titleLabel = new JLabel("Warehouse Login", SwingConstants.CENTER);
        add(titleLabel);
        add(new JLabel("")); // empty space

        JButton managerLogin = new JButton("Manager Login");
        managerLogin.addActionListener((ActionEvent e) ->
            mainGUI.showPanel(new ManagerMenuPanel(mainGUI))
        );
        add(managerLogin);

        JButton clerkLogin = new JButton("Clerk Login");
        clerkLogin.addActionListener((ActionEvent e) ->
            mainGUI.showPanel(new ClerkMenuPanel(mainGUI))
        );
        add(clerkLogin);

        JButton clientLogin = new JButton("Client Login");
        clientLogin.addActionListener((ActionEvent e) -> {
            String clientId = JOptionPane.showInputDialog(mainGUI, "Enter Client ID:");
            Client client = Warehouse.instance().searchClient(clientId);
            if (client != null) {
                mainGUI.showPanel(new ClientMenuPanel(mainGUI, client));
            } else {
                JOptionPane.showMessageDialog(mainGUI, "Client not found.");
            }
        });
        add(clientLogin);
    }
}
