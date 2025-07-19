package pushDownAutomataGraphics.figures;

import java.awt.*;

public class Square <K> implements Figure{
    public int x;
    public int y;
    public int sideLength;
    private final K key;
    public Color color = Color.WHITE;

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(this.color);
        g2d.fillRect(x, y, sideLength, sideLength);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, sideLength, sideLength);
        Font f1 = new Font("Arial", Font.BOLD, 20);
        g2d.setFont(f1);
        g2d.setColor(Color.BLACK);
        String text = (key.toString() != null)? key.toString() : "";
        g2d.drawString(text, x + sideLength / 2 - sideLength / 10, y + sideLength / 2);
    }

    public Square(K key, int x, int y, int sideLength) {
        this.key = key;
        this.x = x;
        this.y = y;
        this.sideLength = sideLength;
    }

    public K getKey() {
        return key;
    }


}
