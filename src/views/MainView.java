package views;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    public MainView() {
        setTitle("OOP UADE");
        setSize(500, 300);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        JLabel label = new JLabel("Hola mundo!", SwingConstants.CENTER);

        label.setFont(new Font("Arial", Font.BOLD, 28));

        add(label);

        setVisible(true);
    }
}