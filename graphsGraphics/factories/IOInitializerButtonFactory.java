package graphsGraphics.factories;

import graphs.AbstractGraph;
import graphsGraphics.buttonLogic.ButtonInitializer;
import graphsGraphics.buttonLogic.IOButtonInitializer;

import java.util.List;

public class IOInitializerButtonFactory implements ButtonInitializerFactory{
    public <K, L> ButtonInitializer<K, L> createInitializer(List<AbstractGraph<K, L>> graphs) {
        return new IOButtonInitializer<>(graphs);
    }
}
