package menu;

import util.Util;

import javax.swing.*;
import java.awt.*;

public class TotalMenu extends JFrame {

    public TotalMenu() {
        super();
        initializeMenu();

        JButton graphButton = Util.createButton(300, 100, "Grafi");

        graphButton.addActionListener(e -> new GraphMenu());

        this.add(graphButton);
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
