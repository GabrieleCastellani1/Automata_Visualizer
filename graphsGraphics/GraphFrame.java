package graphsGraphics;

import graphs.AbstractGraph;
import util.Util;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GraphFrame extends JFrame {

    public <K> GraphFrame(List<AbstractGraph<K>> graphs) {
        super();
        Dimension preferredDimension = new Dimension(Util.FRAMEWIDTH, Util.FRAMEHEIGHT);
        this.setPreferredSize(preferredDimension);
        this.setResizable(true);
        this.setVisible(true);
        this.setBackground(Color.WHITE);

        GraphPanelFactory factory = new GraphPanelFactory();
        AbstractGraphPanel<K> panel = factory.createGraphPanel(graphs);
        GraphButtonPanel buttonPanel = factory.createGraphButtonPanel(graphs);

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
