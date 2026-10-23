package com.lumi2021.urna.view;

import javax.swing.*;
import java.awt.*;

public class BulletinView extends JFrame {
    public BulletinView(String content) {
        super("Boletim de Urna");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(370, 700);
        setMinimumSize(new Dimension(360, 480));
        setLocationRelativeTo(null);

        initializeUI(content);
    }

    private void initializeUI(String content) {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        JTextArea title = new JTextArea(content);
        title.setForeground(Color.BLACK);
        title.setBorder(
                BorderFactory.createEmptyBorder(16, 20, 16, 20)
        );
        title.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));


        root.add(title, BorderLayout.NORTH);

        setContentPane(root);
    }
}

