package graphsGraphics;

import graphs.AbstractGraph;
import graphsGraphics.buttonLogic.ButtonInitializerFactory;
import graphsGraphics.buttonLogic.buttonInitializers.ButtonConfiguration;
import graphsGraphics.buttonLogic.buttonInitializers.ButtonInitializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GraphPanelFactory {
    public <K> AbstractGraphPanel<K> createGraphPanel(List<AbstractGraph<K>> graphs) {
        return new AbstractGraphPanel<>(graphs);
    }

    public <K> GraphButtonPanel createGraphButtonPanel(List<AbstractGraph<K>> graphs) {
        List<ButtonConfiguration> components = initializeButtons(graphs);
        return new GraphButtonPanel(components);
    }

    private <K> List<ButtonConfiguration> initializeButtons(List<AbstractGraph<K>> graphs) {
        ButtonInitializerFactory buttonInitializerFactory = new ButtonInitializerFactory();
        List<ButtonConfiguration> configurations = new ArrayList<>(buttonInitializerFactory.createIOInitializer(graphs).getAllComponents());
        configurations.addAll(buttonInitializerFactory.createBasicGraphButtonInitializer(graphs).getAllComponents());
        for (AbstractGraph<?> graph : graphs) {
            ButtonInitializer initializer = buttonInitializerFactory.createButtonInitializer(graph);
            if (Objects.nonNull(initializer)) {
                configurations.addAll(initializer.getAllComponents());
            }
        }
        return configurations;
    }
}
