package graphsGraphics;

import graphs.AbstractGraph;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AbstractGraphPanel<K> extends JPanel {
    List<AbstractGraph<K>> graphs;

    public AbstractGraphPanel(List<AbstractGraph<K>> graphs) {
        super();
        this.graphs = graphs;
        this.setBackground(Color.WHITE);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);
        try {
            graphs.forEach(graph -> graph.drawGraph(g));
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        } finally {
            repaint();
        }
    }
}
