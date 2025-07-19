package graphs.factories;

import graphs.AbstractGraph;
import graphs.Node;
import graphs.NotOrientedGraph;
import graphs.OrientedGraph;

import java.util.ArrayList;
import java.util.List;

public class OrientedGraphFactory implements GraphFactory{
    public <K, L> List<AbstractGraph<K, L>> createGraph(EntityParams<K> params) {
        List<AbstractGraph<K, L>> list = new ArrayList<>();
        list.add(new OrientedGraph<>(params.getNodes()));
        return list;
    }
}
