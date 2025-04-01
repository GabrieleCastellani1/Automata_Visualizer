package graphsGraphics.buttonLogic;

import graphs.AbstractGraph;
import graphs.OrientedGraph;
import graphs.WeightedGraph;
import graphsGraphics.buttonLogic.buttonInitializers.*;

import java.util.List;


public class ButtonInitializerFactory {
    public ButtonInitializer createButtonInitializer(AbstractGraph<?> graph) {
        if (graph instanceof OrientedGraph) {
            return new OrientedGraphButtonInitializer((OrientedGraph<?>) graph);
        } else if (graph instanceof WeightedGraph) {
            return new WeightedGraphButtonInitializer((WeightedGraph<?>) graph);
        } else {
            return null;
        }
    }

    public <K> ButtonInitializer createIOInitializer(List<AbstractGraph<K>> graphs) {
        return new IOButtonInitializer<>(graphs);
    }

    public <K> ButtonInitializer createBasicGraphButtonInitializer(List<AbstractGraph<K>> graphs) {
        return new BasicGraphButtonInitializer(graphs);
    }
}
