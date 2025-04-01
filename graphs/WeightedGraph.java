package graphs;

import graphs.disjointedSets.DisjointSet;
import util.Util;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.*;

public class WeightedGraph<K> extends AbstractGraph<K> {

    private final List<WeightedEdge> weightedEdges;
    private final EdgeInsertType type;

    public WeightedGraph(List<Node<K>> nodes, List<WeightedEdge> weightedEdges, EdgeInsertType type) {
        super(nodes);
        this.weightedEdges = weightedEdges;
        this.type = type;
    }

    @Override
    public void drawGraph(Graphics g) {
        for (WeightedEdge edge : weightedEdges) {
            int addx = Math.min(edge.n1().getX(), edge.n2().getX());
            int addy = Math.min(edge.n1().getY(), edge.n2().getY());
            int x = Math.abs(edge.n1().getX() - edge.n2().getX()) / 2 + addx;
            int y = Math.abs(edge.n1().getY() - edge.n2().getY()) / 2 + addy;
            g.drawString(String.valueOf(edge.weight()), x + x / 20, y + y / 30);
        }
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
        switch (type) {
            case SINGLE:
                addSingleEdge(key1, key2, weight);
                break;
            case DOUBLE:
                addDoubleEdge(key1, key2, weight);
                break;
        }
    }

    public void addSingleEdge(K key1, K key2, Integer weight) {
        Optional<Node<K>> firstOptionalNode = findNode(key1);
        Optional<Node<K>> secondOptionalNode = findNode(key2);
        if (firstOptionalNode.isPresent() && secondOptionalNode.isPresent()) {
            firstOptionalNode.get().nodes.add(secondOptionalNode.get());
            this.weightedEdges.add(new WeightedEdge(firstOptionalNode.get(), secondOptionalNode.get(), weight));
            createForceGraph();
        }
    }

    public void addDoubleEdge(K key1, K key2, Integer weight) {
        Optional<Node<K>> firstOptionalNode = findNode(key1);
        Optional<Node<K>> secondOptionalNode = findNode(key2);
        if (firstOptionalNode.isPresent() && secondOptionalNode.isPresent()) {
            firstOptionalNode.get().nodes.add(secondOptionalNode.get());
            secondOptionalNode.get().nodes.add(firstOptionalNode.get());
            this.weightedEdges.add(new WeightedEdge(firstOptionalNode.get(), secondOptionalNode.get(), weight));
            createForceGraph();
        }
    }

    @Override
    public void deleteNode(Node<K> node) {
        this.nodes.remove(node);
        this.nodes.forEach(n -> n.nodes.remove(node));
        Iterator<WeightedEdge> weightedEdgesIterator = weightedEdges.iterator();
        WeightedEdge w;
        while (weightedEdgesIterator.hasNext()) {
            w = weightedEdgesIterator.next();
            if (w.n1().equals(node) || w.n2().equals(node)) {
                weightedEdgesIterator.remove();
            }
        }
        createForceGraph();
    }


    @Override
    public void deleteEdge(Node<K> node1, Node<K> node2) {
        node1.nodes.remove(node2);
        node2.nodes.remove(node1);
        Optional<WeightedEdge> optionalWeightedEdge = this.weightedEdges.stream()
                .filter(e -> e.n1().equals(node1)
                        &&
                        e.n2().equals(node2))
                .findAny();
        optionalWeightedEdge.ifPresent(this.weightedEdges::remove);
        createForceGraph();
    }

    public List<DisjointSet<Node<K>>> Kruscal() {//crea il mst creando prima |n.nodi| insiemi disgiunti e poi facendone le Union in ordine in base al
        List<DisjointSet<Node<K>>> sets = new ArrayList<>();           //peso degli archi, prende il minimo di questi ogni volta
        for (Node<K> n : nodes) {
            sets.add(DisjointSet.Make(n));
        }
        sortWeightedEdges();

        colorSets(sets);
        Util.waitAction(1000);

        for (WeightedEdge edge : weightedEdges) {
            Optional<DisjointSet<Node<K>>> d1 = sets.stream().filter(d -> d.key.equals(edge.n1())).findAny();
            Optional<DisjointSet<Node<K>>> d2 = sets.stream().filter(d -> d.key.equals(edge.n2())).findAny();
            if (!d1.get().Find().equals(d2.get().Find())) {
                d1.get().Union(d2.get());
            }

            colorSets(sets);
            Util.waitAction(1000);
        }

        rollback();
        return sets;
    }

    private void colorSets(List<DisjointSet<Node<K>>> sets) {
        for (DisjointSet<Node<K>> set : sets) {
            Random r = new Random();
            float h = r.nextFloat();
            float s = r.nextFloat();
            float b = r.nextFloat();
            Color color = Color.getHSBColor(h, s, b);
            DisjointSet<Node<K>> root = set.Find();
            DisjointSet<Node<K>> nextD;
            if (root.key.getColor().equals(Color.WHITE)) {
                set.key.setColor(color);
            } else {
                color = root.key.getColor();
            }
            nextD = set.next;
            while (nextD != null) {
                nextD.key.setColor(color);
                nextD = nextD.next;
            }
        }
    }

    private void sortWeightedEdges() {
        weightedEdges.sort(Comparator.comparingInt(WeightedEdge::weight));
    }

    public Map<Node<K>, Node<K>> Prim(Node<K> s) {
        Map<WeightedEdge, Shape> rectangles = new HashMap<>();
        Map<Node<K>, Integer> key = new HashMap<>();
        Map<Node<K>, Node<K>> Pi = new HashMap<>();
        for (Node<K> u : nodes) {
            key.put(u, Integer.MAX_VALUE);
            Pi.put(u, null);
        }
        key.put(s, 0);
        Queue<Node<K>> Q = new PriorityQueue<>(Comparator.comparingInt(key::get));
        Q.addAll(nodes);
        while (!Q.isEmpty()) {
            Node<K> u = Q.poll();
            addCircle(u);
            List<Node<K>> Adj = u.getNodes();
            int i = 0;
            while (i < Adj.size()) {
                Node<K> v = Adj.get(i);
                WeightedEdge edgeBetweenUandV = weightedEdges.stream()
                        .filter((e) -> e.n1().equals(u) && e.n2().equals(v)).findAny().get();
                if (Q.contains(v) && key.get(v) > edgeBetweenUandV.weight()) {
                    Util.waitAction(1000);
                    key.put(v, edgeBetweenUandV.weight());
                    Pi.put(v, u);
                    removeRectangle(rectangles.get(edgeBetweenUandV));
                    rectangles.put(edgeBetweenUandV, addRectangle(u, v));
                    /*decrease key*/
                    Q.remove(v);
                    Q.add(v);
                    /*decrease key*/
                }
                i += 1;
            }
            removeAllCircles();
            Util.waitAction(1000);
        }
        rollback();
        return Pi;
    }

    public Map<Node<K>, Node<K>> Dijkstra(Node<K> s) {
        Map<WeightedEdge, Shape> rectangles = new HashMap<>();
        Map<Node<K>, Integer> d = new HashMap<>();
        Map<Node<K>, Node<K>> Pi = new HashMap<>();
        for (Node<K> u : nodes) {
            d.put(u, Integer.MAX_VALUE);
            Pi.put(u, null);
        }
        d.put(s, 0);
        Queue<Node<K>> Q = new PriorityQueue<>(Comparator.comparingInt(d::get));
        Q.addAll(nodes);
        while (!Q.isEmpty()) {
            Node<K> u = Q.poll();
            addCircle(u);
            List<Node<K>> Adj = u.getNodes();
            int i = 0;
            while (i < Adj.size()) {
                Node<K> v = Adj.get(i);
                WeightedEdge edgeBetweenUandV = weightedEdges.stream()
                        .filter((e) -> e.n1().equals(u) && e.n2().equals(v)).findAny().get();
                if (Q.contains(v) && d.get(v) > d.get(u) + edgeBetweenUandV.weight()) {
                    Util.waitAction(1000);
                    d.put(v, d.get(u) + edgeBetweenUandV.weight());
                    Pi.put(v, u);
                    removeRectangle(rectangles.get(edgeBetweenUandV));
                    rectangles.put(edgeBetweenUandV, addRectangle(u, v));
                    /*decrease key*/
                    Q.remove(v);
                    Q.add(v);
                    /*decrease key*/
                }
                i += 1;
            }
            removeAllCircles();
            Util.waitAction(1000);
        }
        rollback();
        return Pi;
    }
}
