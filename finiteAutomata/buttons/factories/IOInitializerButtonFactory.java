package finiteAutomata.buttons.factories;


import finiteAutomata.buttons.IOButtonInitializer;
import graphs.AbstractGraph;
import graphsGraphics.buttonLogic.ButtonInitializer;
import graphsGraphics.factories.ButtonInitializerFactory;

import java.util.List;

public class IOInitializerButtonFactory implements ButtonInitializerFactory {
    public <K, L> ButtonInitializer<K, L> createInitializer(List<AbstractGraph<K, L>> graphs) {
        return new IOButtonInitializer<>(graphs);
    }
}

