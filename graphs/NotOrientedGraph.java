package graphs;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public class NotOrientedGraph<K> extends AbstractGraph<K> {

    public NotOrientedGraph(List<Node<K>> nodes) {
        super(nodes);
    }

    @Override
    public void drawGraph(Graphics g) {
        super.drawGraph(g);
    }

    @Override
    public Optional<Node<K>> findNode(K key) {
        return nodes.stream().filter(n -> n.getData().equals(key)).findAny();
    }

    @Override
    public void addNode(K key) {
        Node<K> node = new Node<>(key);
        this.nodes.add(node);
        this.setRandomNodePosition(node);
        this.createForceGraph();
    }

    @Override
    public void addEdge(K key1, K key2, Integer weight) {
        Optional<Node<K>> firstOptionalNode = findNode(key1);
        Optional<Node<K>> secondOptionalNode = findNode(key2);
        if (firstOptionalNode.isPresent() && secondOptionalNode.isPresent()) {
            firstOptionalNode.get().nodes.add(secondOptionalNode.get());
            secondOptionalNode.get().nodes.add(firstOptionalNode.get());
            createForceGraph();
        }
    }

    @Override
    public void deleteNode(Node<K> node) {
        this.nodes.remove(node);
        this.nodes.forEach(n -> n.nodes.remove(node));
        createForceGraph();
    }

    @Override
    public void deleteEdge(Node<K> node1, Node<K> node2) {
        node1.nodes.remove(node2);
        node2.nodes.remove(node1);
        createForceGraph();
    }
}
