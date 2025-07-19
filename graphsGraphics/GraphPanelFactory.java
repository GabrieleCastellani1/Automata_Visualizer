package graphsGraphics;

import graphs.AbstractGraph;
import graphs.OrientedGraph;
import graphs.WeightedGraph;
import graphsGraphics.buttonLogic.ButtonConfiguration;
import graphsGraphics.buttonLogic.ButtonInitializer;
import graphsGraphics.factories.BasicInitializerButtonFactory;
import graphsGraphics.factories.IOInitializerButtonFactory;
import graphsGraphics.factories.OrientedButtonInitializerFactory;
import graphsGraphics.factories.WeightedButtonInitializerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: this might become an abstract factory if you really need it, otherwise can be removed and only the abstract classes will remain
 * **/
public class GraphPanelFactory {
    public <K, L> AbstractGraphPanel<K, L> createGraphPanel(List<AbstractGraph<K, L>> graphs) {
        return new AbstractGraphPanel<>(graphs);
    }

    public <K, L> GraphButtonPanel createGraphButtonPanel(List<AbstractGraph<K, L>> graphs) {
        List<ButtonConfiguration> components = initializeButtons(graphs);
        return new GraphButtonPanel(components);
    }

    private <K, L> List<ButtonConfiguration> initializeButtons(List<AbstractGraph<K, L>> graphs) {
        List<ButtonConfiguration> configurations = new ArrayList<>(new IOInitializerButtonFactory().createInitializer(graphs).getAllComponents());
        configurations.addAll(new BasicInitializerButtonFactory().createInitializer(graphs).getAllComponents());
        for (AbstractGraph<K, L> graph : graphs) {
            ButtonInitializer<K, L> initializer;
            if (graph instanceof OrientedGraph) {
                List<AbstractGraph<K, L>> g = new ArrayList<>();
                g.add(graph);
                initializer = new OrientedButtonInitializerFactory().createInitializer(g);
                configurations.addAll(initializer.getAllComponents());
            } else if (graph instanceof WeightedGraph) {
                List<AbstractGraph<K, L>> g = new ArrayList<>();
                g.add(graph);
                initializer = new WeightedButtonInitializerFactory().createInitializer(g);
                configurations.addAll(initializer.getAllComponents());
            }

        }
        return configurations;
    }
}
