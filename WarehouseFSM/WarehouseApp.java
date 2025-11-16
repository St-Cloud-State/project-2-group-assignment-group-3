package WarehouseFSM;

import javax.swing.SwingUtilities;

public class WarehouseApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGUI mainGUI = new MainGUI();

            // Start at login panel
            LoginPanel loginPanel = new LoginPanel(mainGUI);
            mainGUI.showPanel(loginPanel);

            mainGUI.setVisible(true);
        });
    }
}

