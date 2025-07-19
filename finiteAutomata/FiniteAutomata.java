package finiteAutomata;

import graphs.AbstractGraph;
import graphs.Node;
import util.Util;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.List;
import java.util.*;

public class FiniteAutomata<K, L> extends AbstractGraph<K, L> {

    private final List<LabelledEdge<K>> edges;
    private final List<Node<K>> initialNodes;
    private final List<Node<K>> finalNodes;

    public FiniteAutomata(List<Node<K>> nodes, List<LabelledEdge<K>> edges) {
        super(nodes);
        this.edges = edges;
        this.initialNodes = new ArrayList<>();
        this.finalNodes = new ArrayList<>();
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

    public void addInitialNode(K key){
        Node<K> node = new Node<>(key);
        this.nodes.add(node);
        this.initialNodes.add(node);
        this.setRandomNodePosition(node);
        this.createForceGraph();
    }

    public void addFinalNode(K key){
        Node<K> node = new Node<>(key);
        this.nodes.add(node);
        this.finalNodes.add(node);
        this.setRandomNodePosition(node);
        this.createForceGraph();
    }

    @Override
    public void deleteNode(Node<K> node) {
        this.nodes.remove(node);
        this.initialNodes.remove(node);
        this.finalNodes.remove(node);
        this.nodes.forEach(n -> n.getNodes().remove(node));
        Iterator<LabelledEdge<K>> labelledEdgesIterator = edges.iterator();
        LabelledEdge<K> e;
        while (labelledEdgesIterator.hasNext()) {
            e = labelledEdgesIterator.next();
            if (e.n1().equals(node) || e.n2().equals(node)) {
                labelledEdgesIterator.remove();
            }
        }
        createForceGraph();
    }

    @Override
    public void deleteEdge(Node<K> node1, Node<K> node2) {
        node1.getNodes().remove(node2);
        node2.getNodes().remove(node1);
        Optional<LabelledEdge<K>> optionalLabelledEdge = this.edges.stream()
                .filter(e -> e.n1().equals(node1)
                        &&
                        e.n2().equals(node2))
                .findAny();
        optionalLabelledEdge.ifPresent(this.edges::remove);
        createForceGraph();
    }

    @Override
    public void addEdge(K key1, K key2, L weight) {
        Optional<Node<K>> firstOptionalNode = findNode(key1);
        Optional<Node<K>> secondOptionalNode = findNode(key2);
        if (firstOptionalNode.isPresent() && secondOptionalNode.isPresent()) {
            firstOptionalNode.get().getNodes().add(secondOptionalNode.get());
            this.edges.add(new LabelledEdge<>(firstOptionalNode.get(), secondOptionalNode.get(), weight.toString()));
            createForceGraph();
        }
    }

    @Override
    public void drawGraph(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (LabelledEdge<K> edge : edges) {

            if (edge.n1().equals(edge.n2())) {
                drawSelfLoop(edge.n1(), g2d, edge.label());
            }else {
                int addx = Math.min(edge.n1().getX(), edge.n2().getX());
                int addy = Math.min(edge.n1().getY(), edge.n2().getY());
                int x = Math.abs(edge.n1().getX() - edge.n2().getX()) / 2 + addx;
                int y = Math.abs(edge.n1().getY() - edge.n2().getY()) / 2 + addy;
                g2d.drawString(String.valueOf(edge.label()), x + x / 20, y + y / 30);
            }
        }

        for (Node<K> node : nodes) {
            for (Node<K> relative : node.getNodes()) {
                if (!node.equals(relative)) {
                    double angle = calculateAngle(node, relative);
                    drawTriangle(15, relative.getX(), relative.getY(), angle, g2d);
                }
            }
        }

        super.drawGraph(g2d);
    }

    private void drawSelfLoop(Node<K> node, Graphics2D g2d, String label) {
        int x = node.getX() + Util.OVALDIAMETER / 2;
        int y = node.getY() - Util.OVALDIAMETER / 2;

        g2d.drawOval(x, y, Util.OVALDIAMETER, Util.OVALDIAMETER);

        double angle = -Math.PI / 2;
        drawTriangle(7, x - Util.OVALDIAMETER/2, y + Util.OVALDIAMETER/2, angle, g2d);

        g2d.drawString(label, x - Util.OVALDIAMETER/2 , y + Util.OVALDIAMETER/2 - 5);
    }

    private void drawTriangle(int lineLength, int relativeX, int relativeY, double angle, Graphics2D g2d) {

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

    public void evaluateString(String data){
        Iterator<String> chars = new ArrayList<>(Arrays.stream(data.split("")).toList()).iterator();
        List<Node<K>> starters = computeStarters();
        boolean finalReached = false;
        while(chars.hasNext()){
            String ch = chars.next();
            starters.forEach(this::addCircle);
            Util.waitAction(1000);
            ArrayList<Node<K>> intermediateNodes = new ArrayList<>();
            for (Node<K> node : starters){
                for (LabelledEdge<K> edge : edges){
                    if(edge.n1().equals(node) && (Objects.equals(edge.label(), "&") || Objects.equals(edge.label(), ch)) && !(intermediateNodes.contains(edge.n2()))){
                        intermediateNodes.add(edge.n2());
                        if(finalNodes.contains(edge.n2())){
                            finalReached = true;
                            addCircle(edge.n2());
                            Util.waitAction(1000);
                        }
                    }
                }
            }
            removeAllCircles();
            starters = intermediateNodes;
            if(finalReached){
                break;
            }
        }
    }

    private List<Node<K>> computeStarters(){
        List<Node<K>> starters = new ArrayList<>(List.copyOf(initialNodes));
        for (Node<K> node : initialNodes){
            starters.addAll(checkConnections(node, new ArrayList<>()));
        }
        for (Node<K> node : starters){
            System.out.println(node.getData());
        }
        return starters;
    }

    private ArrayList<Node<K>> checkConnections(Node<K> starter, ArrayList<Node<K>> visited){
        visited.add(starter);
        for (LabelledEdge<K> edge : edges){
            if(edge.n1().equals(starter) & edge.label() == "&" & !visited.contains(edge.n2())){
                checkConnections(edge.n2(), visited);
            }
        }
        return visited;
    }
}
