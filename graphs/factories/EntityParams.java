package graphs.factories;


import graphs.EdgeInsertType;
import graphs.Node;
import graphs.WeightedEdge;

import java.util.List;

public class EntityParams<K> {
    private List<Node<K>> nodes;
    private List<WeightedEdge> edges;
    private EdgeInsertType edgeType;

    private EntityParams() {
        // private constructor to enforce use of the builder
    }

    public List<Node<K>> getNodes() {
        return nodes;
    }

    public List<WeightedEdge> getEdges() {
        return edges;
    }

    public EdgeInsertType getEdgeType() {
        return edgeType;
    }

    public static <K> Builder<K> builder() {
        return new Builder<>();
    }

    public static class Builder<K> {
        private List<Node<K>> nodes;
        private List<WeightedEdge> edges;
        private EdgeInsertType edgeType;

        public Builder<K> nodes(List<Node<K>> nodes) {
            this.nodes = nodes;
            return this;
        }

        public Builder<K> edges(List<WeightedEdge> edges) {
            this.edges = edges;
            return this;
        }

        public Builder<K> edgeType(EdgeInsertType edgeType) {
            this.edgeType = edgeType;
            return this;
        }

        public EntityParams<K> build() {
            EntityParams<K> params = new EntityParams<>();
            params.nodes = this.nodes;
            params.edges = this.edges;
            params.edgeType = this.edgeType;
            return params;
        }
    }
}

