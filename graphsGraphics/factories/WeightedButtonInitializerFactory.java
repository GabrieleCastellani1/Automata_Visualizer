package graphsGraphics.factories;

import graphs.AbstractGraph;
import graphsGraphics.buttonLogic.ButtonInitializer;
import graphsGraphics.buttonLogic.WeightedGraphButtonInitializer;

import java.util.List;

public class WeightedButtonInitializerFactory implements ButtonInitializerFactory{
    @Override
    public <K, L> ButtonInitializer<K ,L> createInitializer(List<AbstractGraph<K, L>> graphs) {
        return new WeightedGraphButtonInitializer<>(graphs);
    }
}
