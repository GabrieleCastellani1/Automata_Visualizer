package finiteAutomata;

import finiteAutomata.buttons.factories.IOInitializerButtonFactory;
import graphs.AbstractGraph;
import graphsGraphics.AbstractGraphPanel;
import graphsGraphics.GraphButtonPanel;
import graphsGraphics.buttonLogic.ButtonInitializer;
import util.Util;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FAFrame extends JFrame {

    public <K, L> FAFrame(List<AbstractGraph<K, L>> graphs) {
        super();
        Dimension preferredDimension = new Dimension(Util.FRAMEWIDTH, Util.FRAMEHEIGHT);
        this.setPreferredSize(preferredDimension);
        this.setResizable(true);
        this.setVisible(true);
        this.setBackground(Color.WHITE);

        AbstractGraphPanel<K, L> panel = new AbstractGraphPanel<>(graphs);
        ButtonInitializer<K,L> buttonInitializer = new IOInitializerButtonFactory().createInitializer(graphs);
        GraphButtonPanel buttonPanel = new GraphButtonPanel(buttonInitializer.getAllComponents());

        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(preferredDimension);

        JScrollPane scrollPane = new JScrollPane(
                panel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        JViewport viewport = new JViewport() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                panel.paintComponent(g);
                repaint();
            }
        };

        viewport.setView(panel);
        scrollPane.setViewport(viewport);

        JSplitPane container = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        container.setTopComponent(scrollPane);
        container.setBottomComponent(buttonPanel);
        container.setDividerLocation(Util.FRAMEWIDTH / 2);
        container.setPreferredSize(new Dimension(Util.FRAMEWIDTH, Util.FRAMEHEIGHT));
        container.setVisible(true);

        this.add(container);
        this.pack();
    }
}

