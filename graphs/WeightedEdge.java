package graphs;

public class WeightedEdge {
    private final Node<?> n1;
    private final Node<?> n2;
    private final Integer weight;

    public WeightedEdge(Node<?> n1, Node<?> n2, Integer weight) {
        this.n1 = n1;
        this.n2 = n2;
        this.weight = weight;
    }

    public Node<?> n1() {
        return n1;
    }

    public Node<?> n2() {
        return n2;
    }

    public int weight() {
        return weight;
    }
}
