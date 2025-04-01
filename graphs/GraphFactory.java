package graphs;

import java.util.ArrayList;
import java.util.List;

public class GraphFactory {
    public <K> NotOrientedGraph<K> createNotOrientedGraph(List<Node<K>> nodes) {
        return new NotOrientedGraph<>(nodes);
    }

    public <K> OrientedGraph<K> createOrientedGraph(List<Node<K>> nodes) {
        return new OrientedGraph<>(nodes);
    }

    public <K> WeightedGraph<K> createWeightedGraph(List<Node<K>> nodes, List<WeightedEdge> edges, EdgeInsertType type) {
        return new WeightedGraph<>(nodes, edges, type);
    }

    public <K> List<AbstractGraph<K>> createWeightedOrientedGraph() {
        ArrayList<Node<K>> list = new ArrayList<>();
        return List.of(this.createOrientedGraph(list), this.createWeightedGraph(list, new ArrayList<>(), EdgeInsertType.SINGLE));
    }
}
