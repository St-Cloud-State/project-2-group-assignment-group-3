package WarehouseFSM;

import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {
    public MainGUI() {
        setTitle("Warehouse Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // center on screen
        setLayout(new BorderLayout());
    }

    public void showPanel(JPanel panel) {
        setContentPane(panel);
        revalidate();
        repaint();
    }
}
