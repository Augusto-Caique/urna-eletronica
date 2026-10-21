package com.lumi2021.urna;

import com.lumi2021.urna.controller.poolWorker.EmbeddedPoolWorkerController;
import com.lumi2021.urna.view.*;
import java.awt.event.*;
import javax.swing.*;

public class Program {

    static PoolWorkerView poolWorkerView;
    static VoterView voterView;

    public static void main() {

        var a = new EmbeddedPoolWorkerController();

        SwingUtilities.invokeLater(() -> {
            poolWorkerView = new PoolWorkerView();
            voterView = new VoterView();

            WindowAdapter closeHandler = new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            };

            poolWorkerView.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            voterView.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

            poolWorkerView.addWindowListener(closeHandler);
            voterView.addWindowListener(closeHandler);

            poolWorkerView.setVisible(true);
            voterView.setVisible(true);
        });
    }

}
