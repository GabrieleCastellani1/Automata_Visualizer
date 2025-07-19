package graphs.factories;

import graphs.AbstractGraph;

import java.util.List;

public class WeightedOrientedGraphFactory implements GraphFactory{
    public <K, L> List<AbstractGraph<K, L>> createGraph(EntityParams<K> params) {
        List<AbstractGraph<K,L>> orientedGraphList = new OrientedGraphFactory().createGraph(params);
        List<AbstractGraph<K,L>> weightedGraphList = new WeightedGraphFactory().createGraph(params);
        AbstractGraph<K,L> weightedGraph = weightedGraphList.get(0);
        orientedGraphList.add(weightedGraph);
        return orientedGraphList;
    }
}
