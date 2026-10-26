package com.lumi2021.urna.view;

import javax.swing.*;
import java.awt.*;

public class VotingScreen extends JPanel {

    private static final Color BG = new Color(250, 250, 245);
    private static final Color GREEN = new Color(0, 105, 65);
    private static final Color TEXT = new Color(30, 30, 30);

    private String number = "";

    public VotingScreen() {
        setBackground(BG);
        setPreferredSize(new Dimension(800, 500));
        setFocusable(true);

        for (int i = 0; i <= 9; i++) {
            String digit = String.valueOf(i);

            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(digit), "digit" + digit);

            getActionMap().put("digit" + digit, new AbstractAction() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (number.length() < 5) {
                        number += digit;
                        repaint();
                    }
                }
            });
        }

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("BACK_SPACE"), "backspace");

        getActionMap().put("backspace", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (!number.isEmpty()) {
                    number = number.substring(0, number.length() - 1);
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        double scale = Math.min(
                getWidth() / 800.0,
                getHeight() / 500.0
        );

        g2.translate(
                (getWidth() - 800 * scale) / 2,
                (getHeight() - 500 * scale) / 2
        );
        g2.scale(scale, scale);

        // Cabeçalho
        g2.setColor(GREEN);
        g2.fillRect(0, 0, 800, 55);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        g2.drawString("JUSTIÇA ELEITORAL", 24, 35);

        // Identificação do cargo
        g2.setColor(TEXT);
        g2.setFont(new Font("SansSerif", Font.BOLD, 25));
        g2.drawString("SEU VOTO PARA", 30, 105);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 22));
        g2.drawString("Vereador", 30, 140);

        g2.setColor(new Color(190, 190, 190));
        g2.drawLine(30, 160, 770, 160);

        // Campo numérico
        g2.setColor(TEXT);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 17));
        g2.drawString("Número:", 30, 200);

        int x = 30;
        int y = 215;
        int size = 55;
        int gap = 8;

        for (int i = 0; i < 5; i++) {
            int bx = x + i * (size + gap);

            g2.setColor(Color.WHITE);
            g2.fillRect(bx, y, size, size);

            g2.setColor(TEXT);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRect(bx, y, size, size);

            if (i < number.length()) {
                String digit = number.substring(i, i + 1);
                g2.setFont(new Font("Monospaced", Font.BOLD, 32));

                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(
                        digit,
                        bx + (size - fm.stringWidth(digit)) / 2,
                        y + 38
                );
            }
        }

        // Desenhar candidato
        if (number.length() == 5) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 21));
            g2.drawString("JOÃO DA SILVA", 30, 320);

            g2.setFont(new Font("SansSerif", Font.PLAIN, 16));
            g2.drawString("Partido: EXEMPLO", 30, 350);

            g2.setColor(new Color(225, 225, 225));
            g2.fillRect(560, 185, 180, 210);

            g2.setColor(new Color(180, 140, 110));
            g2.fillOval(620, 215, 60, 70);

            g2.setColor(new Color(55, 85, 125));
            g2.fillRoundRect(590, 295, 120, 90, 30, 30);
        } else {
            g2.setColor(new Color(90, 90, 90));
            g2.setFont(new Font("SansSerif", Font.PLAIN, 16));
            g2.drawString("Digite o número do candidato.", 30, 310);
        }

        // Rodapé de instruções na tela
        g2.setColor(new Color(235, 235, 230));
        g2.fillRect(0, 425, 800, 75);

        g2.setColor(TEXT);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g2.drawString("Digite o número do candidato usando o teclado.", 25, 452);
        g2.drawString("BACKSPACE: apagar o último dígito", 25, 477);

        g2.dispose();
    }
}