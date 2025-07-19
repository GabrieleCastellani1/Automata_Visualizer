package pushDownAutomataGraphics.figures;

import java.awt.*;

public class Rectangle implements Figure{

    public int x;
    public int y;
    public int width;
    public int height;
    Color color = Color.BLACK;

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.drawRect(x, y, width, height);
    }
}
