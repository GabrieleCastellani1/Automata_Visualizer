package graphsGraphics.factories;

import graphs.AbstractGraph;
import graphsGraphics.buttonLogic.ButtonInitializer;

import java.util.List;

public interface ButtonInitializerFactory {
    public <K, L> ButtonInitializer createInitializer(List<AbstractGraph<K, L>> graphs);
}
