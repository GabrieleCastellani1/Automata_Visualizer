package graphs.factories;

import graphs.AbstractGraph;

import java.util.List;

public interface GraphFactory {
    public <K, L> List<AbstractGraph<K, L>> createGraph(EntityParams<K> params);
}
