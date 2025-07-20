// Pin.java - Modern arrow/pin design facing downwards
package pushDownAutomataGraphics.figures;

import java.awt.*;
import java.awt.geom.Path2D;

public class Pin implements Figure{
    int x;
    public int y;

    public Pin(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Graphics2D g2d){
        // Enable antialiasing for smooth lines
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create a modern arrow/pin shape facing downwards
        Path2D.Float pin = new Path2D.Float();

        // Arrow body (shaft) - from top to bottom
        pin.moveTo(x, y - 25);
        pin.lineTo(x, y);

        // Arrow head pointing downwards
        pin.moveTo(x - 8, y - 7);
        pin.lineTo(x, y);
        pin.lineTo(x + 8, y - 7);

        // Set stroke for thicker, more visible lines
        g2d.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(new Color(220, 53, 69)); // Modern red color
        g2d.draw(pin);

        // Add a small circle at the top for better visual anchor
        g2d.setColor(new Color(220, 53, 69, 180));
        g2d.fillOval(x - 4, y - 29, 8, 8);
        g2d.setColor(new Color(220, 53, 69));
        g2d.drawOval(x - 4, y - 29, 8, 8);

        // Reset stroke
        g2d.setStroke(new BasicStroke(1.0f));
    }
}