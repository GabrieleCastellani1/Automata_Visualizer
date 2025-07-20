package pushDownAutomataGraphics.figures;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Rectangle implements Figure{

    public int x;
    public int y;
    public int width;
    public int height;
    Color color = new Color(0, 123, 255, 60); // Modern blue with transparency

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Graphics2D g2d) {
        // Enable antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create rounded rectangle
        RoundRectangle2D.Float roundRect = new RoundRectangle2D.Float(x, y, width, height, 12, 12);

        // Fill with gradient background
        GradientPaint gradient = new GradientPaint(
                x, y, new Color(0, 123, 255, 40),
                x, y + height, new Color(0, 123, 255, 80)
        );
        g2d.setPaint(gradient);
        g2d.fill(roundRect);

        // Draw border with modern color
        g2d.setColor(new Color(0, 123, 255, 150));
        g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.draw(roundRect);

        // Add subtle inner glow effect
        g2d.setColor(new Color(255, 255, 255, 30));
        g2d.setStroke(new BasicStroke(1.0f));
        RoundRectangle2D.Float innerGlow = new RoundRectangle2D.Float(x + 1, y + 1, width - 2, height - 2, 10, 10);
        g2d.draw(innerGlow);

        // Reset stroke
        g2d.setStroke(new BasicStroke(1.0f));
    }
}