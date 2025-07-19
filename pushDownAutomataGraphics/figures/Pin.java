package pushDownAutomataGraphics.figures;

import java.awt.*;

public class Pin implements Figure{
    int x;
    public int y;

    public Pin(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Graphics2D g2d){
        g2d.drawLine(x, y, x-20, y-20);
        g2d.drawLine(x, y, x+20, y-20);
        g2d.drawLine(x-20, y-20, x+20, y-20);
    }
}
