package pushDownAutomataGraphics.figures;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Square <K> implements Figure{
    public int x;
    public int y;
    public int sideLength;
    private final K key;
    public Color color = new Color(248, 249, 250); // Light gray background

    @Override
    public void draw(Graphics2D g2d) {
        // Enable antialiasing for smooth rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Draw drop shadow
        g2d.setColor(new Color(0, 0, 0, 20));
        RoundRectangle2D.Float shadow = new RoundRectangle2D.Float(x + 2, y + 2, sideLength, sideLength, 8, 8);
        g2d.fill(shadow);

        // Create rounded rectangle for modern look
        RoundRectangle2D.Float roundSquare = new RoundRectangle2D.Float(x, y, sideLength, sideLength, 8, 8);

        // Fill with gradient background
        GradientPaint gradient = new GradientPaint(
                x, y, color,
                x, y + sideLength, darkenColor(color, 0.95f)
        );
        g2d.setPaint(gradient);
        g2d.fill(roundSquare);

        // Draw modern border
        g2d.setColor(new Color(206, 212, 218));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.draw(roundSquare);

        // Add subtle inner highlight
        g2d.setColor(new Color(255, 255, 255, 40));
        g2d.setStroke(new BasicStroke(1.0f));
        RoundRectangle2D.Float highlight = new RoundRectangle2D.Float(x + 1, y + 1, sideLength - 2, sideLength - 2, 6, 6);
        g2d.draw(highlight);

        // Draw text with modern typography
        String text = (key != null && key.toString() != null) ? key.toString() : "";
        if (!text.isEmpty()) {
            Font font = new Font("Segoe UI", Font.BOLD, Math.max(12, sideLength / 4));
            g2d.setFont(font);

            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getAscent();

            // Center the text properly
            int textX = x + (sideLength - textWidth) / 2;
            int textY = y + (sideLength - textHeight) / 2 + textHeight;

            // Draw text shadow
            g2d.setColor(new Color(0, 0, 0, 30));
            g2d.drawString(text, textX + 1, textY + 1);

            // Draw main text
            g2d.setColor(new Color(33, 37, 41)); // Dark gray for better readability
            g2d.drawString(text, textX, textY);
        }

        // Reset stroke
        g2d.setStroke(new BasicStroke(1.0f));
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

    // Helper method to darken colors for gradient effect
    private Color darkenColor(Color color, float factor) {
        return new Color(
                Math.max(0, (int)(color.getRed() * factor)),
                Math.max(0, (int)(color.getGreen() * factor)),
                Math.max(0, (int)(color.getBlue() * factor)),
                color.getAlpha()
        );
    }
}