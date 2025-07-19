package Menu;

import finiteAutomata.FAFrame;
import finiteAutomata.FiniteAutomata;
import pushDownAutomata.PushDownAutomata;
import pushDownAutomataGraphics.PDATransitionGUI;
import turingMachine.MultiTapeTuringMachineCreatorGUI;
import util.Util;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TotalMenu extends JFrame {

    public TotalMenu() {
        super();
        initializeMenu();

        JButton FAButton = Util.createButton(300, 100, "Finite Automata");

        FAButton.addActionListener(e -> new FAFrame(List.of(new FiniteAutomata<>(new ArrayList<>(), new ArrayList<>()))));

        JButton pdaButton = Util.createButton(300, 100+Util.BUTTONHEIGHT, "Push-down Automata");

        pdaButton.addActionListener(e -> new PDATransitionGUI<>(new PushDownAutomata<>()));

        JButton turingButton = Util.createButton(300, 100+2*Util.BUTTONHEIGHT, "Turing Machine");

        turingButton.addActionListener(ev -> SwingUtilities.invokeLater(() -> {
            try {
            } catch (Exception e) {
                System.err.println("Failed to set look and feel: " + e.getMessage());
                // Continue with default look and feel
            }

            // Create and display the GUI
            new MultiTapeTuringMachineCreatorGUI().setVisible(true);
        }));

        this.add(FAButton);
        this.add(pdaButton);
        this.add(turingButton);
    }

    private void initializeMenu() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(Util.FRAMEWIDTH, Util.FRAMEHEIGHT);
        this.setResizable(true);
        this.setVisible(true);
        this.setBackground(Color.WHITE);
        this.setLayout(null);
    }
}
