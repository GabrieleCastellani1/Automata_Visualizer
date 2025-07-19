package graphsGraphics.factories;

import graphs.AbstractGraph;
import graphsGraphics.buttonLogic.BasicGraphButtonInitializer;
import graphsGraphics.buttonLogic.ButtonInitializer;


import java.util.List;

public class BasicInitializerButtonFactory implements ButtonInitializerFactory{
    public <K, L> ButtonInitializer<K, L> createInitializer(List<AbstractGraph<K, L>> graphs) {
        return new BasicGraphButtonInitializer<>(graphs);
    }
}
