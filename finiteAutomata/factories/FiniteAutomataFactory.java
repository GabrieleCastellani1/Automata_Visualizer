package finiteAutomata.factories;

import graphs.AbstractGraph;
import graphs.NotOrientedGraph;
import graphs.factories.EntityParams;
import graphs.factories.GraphFactory;

import java.util.ArrayList;
import java.util.List;

public class FiniteAutomataFactory implements GraphFactory {
    public <K, L> List<AbstractGraph<K, L>> createGraph(EntityParams<K> params) {
        List<AbstractGraph<K, L>> list = new ArrayList<>();
        list.add(new NotOrientedGraph<>(params.getNodes()));
        return list;
    }
}
