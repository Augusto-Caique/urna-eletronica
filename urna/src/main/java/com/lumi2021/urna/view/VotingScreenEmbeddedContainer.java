package com.lumi2021.urna.view;

import javax.swing.*;
import java.awt.*;

public class VotingScreenEmbeddedContainer extends JFrame {
    private VotingScreen screen = new VotingScreen();

    public VotingScreenEmbeddedContainer() {
        super("Tela da urna");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(screen);

        GraphicsDevice device = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();

        setUndecorated(true);
        device.setFullScreenWindow(this);
    }
}
