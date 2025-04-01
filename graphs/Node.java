package graphs;

import util.Util;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node<K> {
    protected K data;
    protected List<Node<K>> nodes;

    protected Color color;

    protected int x;

    protected int y;

    public Node(K data) {
        this.data = data;
        this.nodes = new ArrayList<>();
        this.color = Color.WHITE;
    }

    public void addEdge(Node<K> node) {
        this.nodes.add(node);
    }

    public K getData() {
        return data;
    }

    public List<Node<K>> getNodes() {
        return nodes;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(this.color);
        g2d.fillOval(x, y, Util.OVALDIAMETER, Util.OVALDIAMETER);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(x, y, Util.OVALDIAMETER, Util.OVALDIAMETER);
        Font f1 = new Font("Arial", Font.BOLD, 15);
        g2d.setFont(f1);
        g2d.setColor(Color.BLACK);
        String text = (data.toString() != null) ? data.toString() : "";
        g2d.drawString(text, x + Util.OVALDIAMETER / 4, y + Util.OVALDIAMETER / 2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node<?> node = (Node<?>) o;
        return Objects.equals(data, node.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}
