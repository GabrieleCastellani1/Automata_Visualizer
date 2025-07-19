package graphs.factories;

import graphs.*;

import java.util.ArrayList;
import java.util.List;

public class WeightedGraphFactory implements GraphFactory{
    public <K, L> List<AbstractGraph<K, L>> createGraph(EntityParams<K> params) {
        List<AbstractGraph<K, L>> list = new ArrayList<>();
        list.add(new WeightedGraph<>(params.getNodes(), params.getEdges(), params.getEdgeType()));
        return list;
    }
}
