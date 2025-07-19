package graphsGraphics.factories;

import graphs.AbstractGraph;
import graphsGraphics.buttonLogic.ButtonInitializer;
import graphsGraphics.buttonLogic.OrientedGraphButtonInitializer;

import java.util.List;

public class OrientedButtonInitializerFactory implements ButtonInitializerFactory{
    @Override
    public <K, L> ButtonInitializer<K, L> createInitializer(List<AbstractGraph<K, L>> graphs) {
        return new OrientedGraphButtonInitializer<>(graphs);
    }
}
