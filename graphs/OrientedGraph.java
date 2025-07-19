package graphs;

import util.Util;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.List;
import java.util.*;

public class OrientedGraph<K, L> extends AbstractGraph<K, L> {
    public OrientedGraph(List<Node<K>> nodes) {
        super(nodes);
    }

    @Override
    public void drawGraph(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for (Node<K> node : nodes) {
            for (Node<K> relative : node.getNodes()) {
                double angle = calculateAngle(node, relative);
                drawTriangle(relative.getX(), relative.getY(), angle, g2d);
            }
        }
        super.drawGraph(g2d);
    }

    private void drawTriangle(int relativeX, int relativeY, double angle, Graphics2D g2d) {
        int lineLength = 15;

        Line2D line1 = new Line2D.Double(
                relativeX,
                relativeY,
                relativeX - lineLength,
                relativeY - lineLength
        );

        Line2D line2 = new Line2D.Double(
                relativeX,
                relativeY,
                relativeX + lineLength,
                relativeY - lineLength
        );

        AffineTransform rotate =
                AffineTransform.getRotateInstance(
                        angle + Math.PI / 2, relativeX, relativeY
                );
        int centerX = relativeX + Util.OVALDIAMETER / 2;
        int centerY = relativeY + Util.OVALDIAMETER / 2;
        double xTranslate = centerX + Math.cos(angle) * Util.OVALDIAMETER / 2;
        double yTranslate = centerY + Math.sin(angle) * Util.OVALDIAMETER / 2;
        AffineTransform translate = AffineTransform.getTranslateInstance(
                xTranslate - relativeX,
                yTranslate - relativeY
        );
        translate.concatenate(rotate);
        g2d.draw(translate.createTransformedShape(line1));
        g2d.draw(translate.createTransformedShape(line2));
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
    public void addEdge(K key1, K key2, L data) {
        Optional<Node<K>> firstOptionalNode = findNode(key1);
        Optional<Node<K>> secondOptionalNode = findNode(key2);
        if (firstOptionalNode.isPresent() && secondOptionalNode.isPresent()) {
            firstOptionalNode.get().nodes.add(secondOptionalNode.get());
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

    public List<Node<K>> DFSTs() {//ritorna il Topological Sort del grafo
        List<Node<K>> list = new ArrayList<>();
        for (Node<K> j : nodes) {
            j.setColor(Color.WHITE);
        }
        for (Node<K> v : nodes) {
            if (v.getColor().equals(Color.WHITE)) {
                list = DFSVisitTs(v, list);
            }
        }
        rollback();
        return list;
    }

    public List<Node<K>> DFSVisitTs(Node<K> v, List<Node<K>> list) {
        v.setColor(Color.LIGHT_GRAY);
        List<Node<K>> Adj = v.getNodes();
        int i = 0;
        while (i < Adj.size()) {
            Node<K> u = Adj.get(i);
            if (u != null && u.getColor().equals(Color.LIGHT_GRAY)) {
                System.out.println("Ho trovato un ciclo");
            }
            if (u != null && u.getColor().equals(Color.WHITE)) {
                list = DFSVisitTs(u, list);
            }
            i += 1;
        }
        v.setColor(Color.DARK_GRAY);
        list.add(v);
        return list;
    }

    public void DFSKosaraju(Map<Node<K>, Node<K>> Pi,
                            Map<Node<K>, Integer> i,
                            Map<Node<K>, Integer> f) {
        int TIME = 0;
        for (Node<K> u : nodes) {
            Pi.put(u, null);
            u.setColor(Color.WHITE);
        }
        for (Node<K> v : nodes) {
            if (v.getColor().equals(Color.WHITE)) {
                TIME = DFSVisitKosaraju(v, TIME, i, f, Pi);
            }
        }
    }

    public int DFSVisitKosaraju(Node<K> v,
                                int TIME,
                                Map<Node<K>, Integer> i,
                                Map<Node<K>, Integer> f,
                                Map<Node<K>, Node<K>> Pi) {
        v.setColor(Color.LIGHT_GRAY);

        i.put(v, TIME);
        TIME = TIME + 1;
        List<Node<K>> Adj = v.getNodes();
        int count = 0;
        while (count < Adj.size()) {
            Node<K> u = Adj.get(count);

            if (u.getColor().equals(Color.WHITE)) {
                Pi.put(u, v);
                TIME = DFSVisitKosaraju(u, TIME, i, f, Pi);
            }
            count += 1;
        }
        f.put(v, TIME);
        TIME += 1;
        v.setColor(Color.DARK_GRAY);
        return TIME;
    }

    public AbstractGraph<K, L> invertOrientedGraph() {
        Map<Node<K>, Node<K>> invertedMap = new HashMap<>();
        for (Node<K> node : nodes) {
            invertedMap.put(node, new Node<>(node.data));
        }
        for (Node<K> node : nodes) {
            node.getNodes().forEach((n) -> invertedMap.get(n).addEdge(node));
        }
        return new OrientedGraph<>(new ArrayList<>(invertedMap.values()));
    }

    public List<List<Node<K>>> Kosaraju() {//ritorna il grafo delle componenti fortemente connesse, grafi ciclici possono così venire considerati aciclici
        Map<Node<K>, Node<K>> Pi = new HashMap<>();
        Map<Node<K>, Integer> i = new HashMap<>();
        Map<Node<K>, Integer> f = new HashMap<>();

        DFSKosaraju(Pi, i, f);
        for (Node<K> n : nodes) {
            n.setColor(Color.WHITE);
        }

        List<List<Node<K>>> SCC = new ArrayList<>();
        OrientedGraph<K, L> GPrimo = (OrientedGraph<K, L>) invertOrientedGraph();

        while (!f.keySet().isEmpty()) {
            int max = 0;
            Node<K> root = null;
            for (Node<K> n : f.keySet()) {
                if (f.get(n) > max) {
                    max = f.get(n);
                    root = n;
                }
            }
            if (Pi.get(root) == null) {
                List<Node<K>> invertedNodeList = new ArrayList<>();
                List<Node<K>> rightNodeList = new ArrayList<>();

                addCircle(nodes.get(nodes.indexOf(GPrimo.nodes.get(GPrimo.nodes.indexOf(root)))));
                Util.waitAction(1000);

                DFSVisitTs(GPrimo.nodes.get(GPrimo.nodes.indexOf(root)), invertedNodeList);
                for (Node<K> node : invertedNodeList) {
                    node.setColor(Color.WHITE);
                }
                invertedNodeList.forEach((el) -> rightNodeList.add(nodes.get(nodes.indexOf(el))));
                SCC.add(rightNodeList);

                randomColorNodes(SCC);
            } else {
                if (!Pi.get(root).getColor().equals(Objects.requireNonNull(root).getColor())) {
                    root.setColor(getRandomColor());
                }
            }
            f.remove(root);
        }
        rollback();
        return SCC;
    }

    private void randomColorNodes(List<List<Node<K>>> SCC) {
        for (List<Node<K>> l : SCC) {
            Color color = getRandomColor();
            for (Node<K> n : l) {
                n.setColor(color);
                Util.waitAction(700);
            }
        }
    }

    public Color getRandomColor() {
        Random r = new Random();
        float h = r.nextFloat();
        float s = r.nextFloat();
        float b = r.nextFloat();
        return Color.getHSBColor(h, s, b);
    }
}
