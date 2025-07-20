package pushDownAutomataGraphics;

import pushDownAutomata.PushDownAutomata;
import util.Util;

import javax.swing.*;
import java.awt.*;

public class PDAFrame<K> extends JFrame {

    public <K> PDAFrame(PushDownAutomata<K> pda){
        super();

        // Modern frame setup
        this.setTitle("Push Down Automata Simulator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(Util.FRAMEWIDTH, Util.FRAMEHEIGHT));
        this.setResizable(true);

        PDAPanel<?> panel = new PDAPanel<>(pda);
        PDAControlPanel<K> controlPanel = new PDAControlPanel<>(pda);

        Dimension preferredDimension = new Dimension(Util.FRAMEWIDTH, Util.FRAMEHEIGHT);
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(preferredDimension);

        // Modern scroll pane with cleaner styling
        JScrollPane scrollPane = new JScrollPane(
                panel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);

        JViewport viewport = new JViewport(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                panel.paintComponent(g);
                repaint();
            }
        };

        viewport.setView(panel);
        scrollPane.setViewport(viewport);

        // Modern split pane with cleaner divider
        JSplitPane container = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        container.setTopComponent(scrollPane);
        container.setBottomComponent(controlPanel);
        container.setDividerLocation(Util.FRAMEWIDTH/2);
        container.setPreferredSize(preferredDimension);
        container.setDividerSize(8);
        container.setBorder(BorderFactory.createEmptyBorder());
        container.setBackground(new Color(248, 249, 250));

        this.add(container);
        this.pack();
        this.setLocationRelativeTo(null); // Center on screen
        this.setVisible(true);
    }
}