package menu;

import enums.GraphType;
import graphs.AbstractGraph;
import graphs.EdgeInsertType;
import graphs.GraphFactory;
import graphsGraphics.GraphFrame;
import util.Util;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GraphMenu extends JFrame {
    public GraphMenu() {
        super();
        initializeMenu();


        JButton abstractGraphButton = Util.createButton(300, 100, 200, 50, "Not Oriented Graph");

        abstractGraphButton.addActionListener(e -> createGraph(GraphType.ABSTRACT_GRAPH));

        JButton orientedGraphButton = Util.createButton(300, 150, 200, 50, "Oriented Graph");

        orientedGraphButton.addActionListener(e -> createGraph(GraphType.ORIENTED_GRAPH));

        JButton weightedNotOrientedGraphButton = Util.createButton(300, 200, 200, 50, "Weighted Not Oriented Graph");

        weightedNotOrientedGraphButton.addActionListener(e -> createGraph(GraphType.WEIGHTED_NOT_ORIENTED_GRAPH));

        JButton weightedOrientedGraphButton = Util.createButton(300, 250, 200, 50, "Weighted Oriented Graph");

        weightedOrientedGraphButton.addActionListener(e -> createGraph(GraphType.WEIGHTED_ORIENTED_GRAPH));

        this.add(abstractGraphButton);
        this.add(orientedGraphButton);
        this.add(weightedNotOrientedGraphButton);
        this.add(weightedOrientedGraphButton);
    }

    private void initializeMenu() {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(Util.FRAMEWIDTH, Util.FRAMEHEIGHT);
        this.setResizable(true);
        this.setVisible(true);
        this.setBackground(Color.WHITE);
        this.setLayout(null);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    private <K> void createGraph(GraphType graphType) {
        List<AbstractGraph<K>> graphs = chooseGraph(graphType);
        JFrame F = new GraphFrame(graphs);
        Util.setLocationToTopRight(F);
    }

    private <K> List<AbstractGraph<K>> chooseGraph(GraphType graphType) {
        GraphFactory factory = new GraphFactory();
        switch (graphType) {
            case ABSTRACT_GRAPH:
                return List.of(factory.createNotOrientedGraph(new ArrayList<>()));
            case ORIENTED_GRAPH:
                return List.of(factory.createOrientedGraph(new ArrayList<>()));
            case WEIGHTED_NOT_ORIENTED_GRAPH:
                return List.of(factory.createWeightedGraph(new ArrayList<>(), new ArrayList<>(), EdgeInsertType.DOUBLE));
            //TODO: fix this shit
            case WEIGHTED_ORIENTED_GRAPH:
                return factory.createWeightedOrientedGraph();
            default:
                return null;
        }
    }
}
