package graphs;

import util.Util;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class AbstractGraph<K, L> {
    public final List<Node<K>> nodes;
    private final Map<Node<K>, Double> xTotalForces;
    private final Map<Node<K>, Double> yTotalForces;

    public Iterator<Node<K>> getNodesIterator() {
        return this.nodes.iterator();
    }

    public abstract Optional<Node<K>> findNode(K key);

    public abstract void addNode(K key);

    public abstract void deleteNode(Node<K> node);

    public abstract void deleteEdge(Node<K> node1, Node<K> node2);

    public abstract void addEdge(K key1, K key2, L data);

    private final List<Shape> highlightRectangles;
    private final List<Ellipse2D> highlightCircles;

    public AbstractGraph(List<Node<K>> nodes) {
        this.nodes = nodes;
        xTotalForces = new HashMap<>();
        yTotalForces = new HashMap<>();
        highlightRectangles = new ArrayList<>();
        highlightCircles = new ArrayList<>();
        setRandomGraphPositions(); // --> SETUP INIZIALE RANDOMICO DELLE POSIZIONI DEI NODI
        createForceGraph();
    }

    public void drawGraph(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (Iterator<Node<K>> it = this.getNodesIterator(); it.hasNext(); ) {
            Node<K> state = it.next();
            int x = state.getX();
            int y = state.getY();
            for (Node<K> relative : state.getNodes()) {
                g2d.drawLine(
                        x + Util.OVALDIAMETER / 2,
                        y + Util.OVALDIAMETER / 2,
                        relative.getX() + Util.OVALDIAMETER / 2,
                        relative.getY() + Util.OVALDIAMETER / 2
                );
                relative.draw(g2d);
            }
            try {
                g2d.setColor(Color.YELLOW);
                for (Shape rectangle : highlightRectangles) {
                    g2d.fill(rectangle);
                }
                g2d.setColor(Color.BLACK);
                for (Ellipse2D circle : highlightCircles) {
                    g2d.draw(circle);
                }
            } catch (ConcurrentModificationException ignored) {

            } finally {
                state.draw(g2d);
            }
        }
    }

    public void addCircle(Node<K> n) {
        this.highlightCircles.add(new Ellipse2D.Double(
                n.getX() - Util.OVALDIAMETER / 2.0,
                n.getY() - Util.OVALDIAMETER / 2.0,
                2 * Util.OVALDIAMETER,
                2 * Util.OVALDIAMETER)
        );
    }

    public Shape addRectangle(Node<K> n1, Node<K> n2) {
        int width = 10;
        int height = (int) Math.sqrt(Math.pow((n2.getX() - n1.getX()), 2) + Math.pow(n2.getY() - n1.getY(), 2));
        Rectangle rectangle = new Rectangle(n1.getX() - width / 2, n1.getY() - width / 2, width, height);
        AffineTransform rotate = AffineTransform.getRotateInstance(
                calculateAngle(n1, n2) + Math.PI / 2,
                n1.getX(),
                n1.getY()
        );

        AffineTransform translate = AffineTransform.getTranslateInstance(
                Util.OVALDIAMETER / 2.0,
                Util.OVALDIAMETER / 2.0
        );
        translate.concatenate(rotate);
        Shape shape = translate.createTransformedShape(rectangle);
        this.highlightRectangles.add(shape);
        return shape;
    }

    public void removeRectangle(Shape s) {
        this.highlightRectangles.remove(s);
    }

    public void removeAllRectangles() {
        this.highlightRectangles.removeAll(this.highlightRectangles);
    }

    public void removeAllCircles() {
        this.highlightCircles.removeAll(this.highlightCircles);
    }

    public void setRandomGraphPositions() {
        for (Node<K> node : nodes) {
            setRandomNodePosition(node);
        }
    }

    public void setRandomNodePosition(Node<?> node) {
        Random randomGenerator = new Random();
        int max = 450;
        int min = 200;
        node.x = randomGenerator.nextInt(max - min + 1) + min;
        node.y = randomGenerator.nextInt(max - min + 1) + min;
    }

    public void createForceGraph() {
        int i = 0;
        getTotalForces();
        double maxForce = Math.max(getMaxForceX(), getMaxForceY());
        while (i < 1000 && maxForce > 0.3) {
            setPos(Util.breakFactor);
            getTotalForces();
            maxForce = Math.max(getMaxForceX(), getMaxForceY());
            i += 1;
        }
    }

    public double getMaxForceX() {
        double max = Double.MIN_VALUE;
        for (Node<K> node : xTotalForces.keySet()) {
            if (Math.abs(xTotalForces.get(node)) > max) {
                max = Math.abs(xTotalForces.get(node));
            }
        }
        return max;
    }

    public double getMaxForceY() {
        double max = Double.MIN_VALUE;
        for (Node<K> node : yTotalForces.keySet()) {
            if (Math.abs(yTotalForces.get(node)) > max) {
                max = Math.abs(yTotalForces.get(node));
            }
        }
        return max;
    }

    public void setPos(double coolingFactor) {
        for (Node<K> node : nodes) {
            node.x = (int) (node.x + (coolingFactor * xTotalForces.get(node)));
            node.y = (int) (node.y + (coolingFactor * yTotalForces.get(node)));
        }
    }

    public void getTotalForces() {
        double cSpring = Util.cSpring;//26
        double l = Util.l;//220
        double cRep = Util.cRep;//45000

        double cGrav = Util.cGrav;//14
        Node<K> gravityNode = new Node<>(null);
        gravityNode.x = 400;
        gravityNode.y = 300;

        BiFunction<Node<K>, Node<K>, Double> repulsiveForce = (n1, n2) -> cRep / // --> repulsiva da n1 verso n2
                (Math.pow(n1.y - n2.y, 2) + Math.pow(n1.x - n2.x, 2));

        BiFunction<Node<K>, Node<K>, Double> attractiveForce = (n1, n2) -> cSpring *
                Math.log(Math.sqrt(// --> attrattiva da n1 verso n2
                        Math.pow(n2.y - n1.y, 2) + Math.pow(n2.x - n1.x, 2)) / l);

        BiFunction<Node<K>, Node<K>, Double> gravityForce = (n1, n2) ->
                (Math.abs(n1.x - n2.x) > 50 && Math.abs(n1.y - n2.y) > 50) ?
                        cGrav / Math.sqrt(Math.pow(n1.y - n2.y, 2) + Math.pow(n1.x - n2.x, 2))
                        :
                        0;

        double currentXForce;
        double currentYForce;
        initializeForces();

        for (Node<K> node : nodes) {
            double xTotalRepulsive = calculateTotalForce(node, findNotAdjacent(node), repulsiveForce, Math::cos);
            double yTotalRepulsive = calculateTotalForce(node, findNotAdjacent(node), repulsiveForce, Math::sin);

            double xTotalAttractive = -calculateTotalForce(node, node.nodes, attractiveForce, Math::cos);
            double yTotalAttractive = -calculateTotalForce(node, node.nodes, attractiveForce, Math::sin);
            setThrowBack(node, node.nodes, attractiveForce);

            double xTotalGravity = gravityForce.apply(node, gravityNode) * Math.cos(calculateAngle(gravityNode, node));
            double yTotalGravity = gravityForce.apply(node, gravityNode) * Math.sin(calculateAngle(gravityNode, node));

            currentXForce = (xTotalForces.get(node) != null) ? xTotalForces.get(node) : 0;
            currentYForce = (yTotalForces.get(node) != null) ? yTotalForces.get(node) : 0;

            xTotalForces.put(node, currentXForce + xTotalAttractive + xTotalRepulsive + xTotalGravity);
            yTotalForces.put(node, currentYForce + yTotalAttractive + yTotalRepulsive + yTotalGravity);
        }

    }

    private void initializeForces() {
        for (Node<K> node : xTotalForces.keySet()) {
            xTotalForces.put(node, 0d);
            yTotalForces.put(node, 0d);
        }
    }

    private List<Node<K>> findNotAdjacent(Node<K> node) {
        List<Node<K>> notAdjacent = new ArrayList<>();
        for (Node<K> n : nodes) {
            if (!n.equals(node) && !node.nodes.contains(n) && !n.nodes.contains(node)) {
                notAdjacent.add(n);
            }
        }
        return notAdjacent;
    }

    private void setThrowBack(Node<K> n1,
                              List<Node<K>> nodes,
                              BiFunction<Node<K>, Node<K>, Double> forceFunction) {

        double currentXForce;
        double currentYForce;
        for (Node<K> n2 : nodes) {
            if(!n1.equals(n2)){
                double xForce = -calculateSingleForce(n2, n1, forceFunction, Math::cos);
                double yForce = -calculateSingleForce(n2, n1, forceFunction, Math::sin);

                currentXForce = (xTotalForces.get(n2) != null) ? xTotalForces.get(n2) : 0;
                currentYForce = (yTotalForces.get(n2) != null) ? yTotalForces.get(n2) : 0;

                xTotalForces.put(n2, currentXForce + xForce);
                yTotalForces.put(n2, currentYForce + yForce);
            }
        }
    }

    private double calculateTotalForce(Node<K> n1,
                                       List<Node<K>> nodes,
                                       BiFunction<Node<K>, Node<K>, Double> forceFunction,
                                       Function<Double, Double> trigFunction) {

        double total = 0;

        for (Node<K> n2 : nodes) {
            if (!n1.equals(n2)) {
                total += calculateSingleForce(n1, n2, forceFunction, trigFunction);
            }
        }

        return total;
    }

    private double calculateSingleForce(Node<K> n1,
                                        Node<K> n2,
                                        BiFunction<Node<K>, Node<K>, Double> forceFunction,
                                        Function<Double, Double> trigFunction) {

        double functionOnAngle = trigFunction.apply(calculateAngle(n1, n2));
        double force = forceFunction.apply(n1, n2);
        return force * functionOnAngle;
    }

    protected double calculateAngle(Node<K> n1, Node<K> n2) {
        return Math.atan2(n1.y - n2.y, n1.x - n2.x);
    }


    public Map<Node<K>, Node<K>> BFS(Node<K> s) {//visita ricorsiva "a livelli" del grafo
        Map<Node<K>, Integer> d = new HashMap<>();
        Map<Node<K>, Node<K>> Pi = new HashMap<>();
        for (Node<K> u : nodes) {
            u.setColor(Color.WHITE);
            d.put(u, Integer.MAX_VALUE);
            Pi.put(u, null);
        }
        Queue<Node<K>> Q = new ArrayDeque<>();
        Q.add(s);
        s.setColor(Color.LIGHT_GRAY);
        d.put(s, 0);
        while (!Q.isEmpty()) {
            Node<K> u = Q.peek();
            List<Node<K>> Adj = u.getNodes();//Attezione! Cambia se il grafo è orientato o meno
            int i = 0;

            addCircle(u);

            while (i < Adj.size()) {
                Node<K> v = Adj.get(i);

                addRectangle(u, v);

                if (v.getColor().equals(Color.LIGHT_GRAY) || v.getColor().equals(Color.DARK_GRAY)) {
                    System.out.println("Ho trovato un ciclo");
                }
                if (v.getColor().equals(Color.WHITE)) {
                    v.setColor(Color.LIGHT_GRAY);
                    Util.waitAction(1000);
                    d.put(v, d.get(u) + 1);
                    Pi.put(v, u);
                    Q.add(v);
                }
                i += 1;
            }

            removeAllCircles();

            u.setColor(Color.DARK_GRAY);
            Util.waitAction(1000);
            Q.poll();
        }
        removeAllRectangles();
        removeAllCircles();
        rollback();
        return Pi;
    }

    public Map<Node<K>, Node<K>> DFS() {//visita ricorsiva "in profondità" del grafo
        Map<Node<K>, Node<K>> Pi = new HashMap<>();
        Map<Node<K>, Integer> i = new HashMap<>();
        Map<Node<K>, Integer> f = new HashMap<>();
        int TIME = 0;
        for (Node<K> u : nodes) {
            u.setColor(Color.WHITE);
            Pi.put(u, null);
        }
        for (Node<K> v : nodes) {
            if (v.getColor().equals(Color.WHITE)) {
                TIME = DFSVisit(v, TIME, i, f, Pi);
            }
        }
        rollback();
        return Pi;
    }

    public int DFSVisit(Node<K> v,
                        int TIME,
                        Map<Node<K>, Integer> i,
                        Map<Node<K>, Integer> f,
                        Map<Node<K>, Node<K>> Pi) {
        v.setColor(Color.LIGHT_GRAY);

        addCircle(v);

        Util.waitAction(1000);
        i.put(v, TIME);
        TIME = TIME + 1;
        List<Node<K>> Adj = v.getNodes();
        int count = 0;
        while (count < Adj.size()) {
            Node<K> u = Adj.get(count);

            addRectangle(v, u);

            if (u.getColor().equals(Color.WHITE)) {
                Pi.put(u, v);

                removeAllCircles();

                TIME = DFSVisit(u, TIME, i, f, Pi);
            }
            count += 1;
        }
        f.put(v, TIME);
        TIME += 1;
        v.setColor(Color.DARK_GRAY);
        Util.waitAction(1000);
        return TIME;
    }

    public void rollback() {
        nodes.forEach(n -> n.setColor(Color.WHITE));
        removeAllCircles();
        removeAllRectangles();
    }
}
