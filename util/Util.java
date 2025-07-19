package util;

import javax.swing.*;
import java.awt.*;

public class Util {

    public static int OVALDIAMETER = 30;
    public static int FRAMEWIDTH = 800;
    public static int FRAMEHEIGHT = 600;
    public static int BUTTONWIDTH = 220;
    public static int BUTTONHEIGHT = 50;
    public static int SIDELENGTH = 70;
    public static final double cSpring = 24;
    public static final double l = 130;
    public static final double cRep = 40000;
    public static final double cGrav = 150;
    public static final double breakFactor = 0.88;

    public static void setLocationToTopRight(JFrame frame) {
        GraphicsConfiguration config = frame.getGraphicsConfiguration();
        Rectangle bounds = config.getBounds();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(config);

        int x = bounds.x + bounds.width - insets.right - frame.getWidth();
        int y = bounds.y + insets.top;
        frame.setLocation(x, y);
    }

    public static void waitAction(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static JButton createButton(int x, int y, String text) {
        JButton Button = new JButton();
        Button.setBounds(x, y, BUTTONWIDTH, BUTTONHEIGHT);
        Button.setMaximumSize(new Dimension(BUTTONWIDTH, BUTTONHEIGHT));
        Button.setPreferredSize(new Dimension(BUTTONWIDTH, BUTTONHEIGHT));
        Button.setText(text);
        return Button;
    }

    public static JButton createButton(int x, int y, int width, int height, String text) {
        JButton Button = new JButton();
        Button.setBounds(x, y, width, height);
        Button.setMaximumSize(new Dimension(width, height));
        Button.setPreferredSize(new Dimension(width, height));
        Button.setText(text);
        return Button;
    }
}
