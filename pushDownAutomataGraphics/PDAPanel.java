package pushDownAutomataGraphics;

import pushDownAutomata.PushDownAutomata;
import pushDownAutomataGraphics.ViewManager.AbstractViewManager;
import pushDownAutomataGraphics.figures.Square;
import util.Util;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.ConcurrentModificationException;

public class PDAPanel<K> extends JPanel {
    private final PushDownAutomata<K> pda;
    private Dimension size;

    public PDAPanel(PushDownAutomata<K> pda){
        this.pda = pda;
        this.size = this.getSize();
        setBackground(new Color(248, 249, 250)); // Modern light background
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Enable antialiasing for smoother rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        this.getSize(size);
        Collection<AbstractViewManager<K>> viewManagers = pda.getViewManagers();
        try {
            int y = 50;
            for(AbstractViewManager<K> viewManager : viewManagers){
                viewManager.setCurrentY(y);
                y += 100;
            }
            viewManagers.forEach(v -> v.getAllFigures().forEach(f -> f.draw(g2d)));
            viewManagers.forEach(v -> v. getAllFigures().forEach(f -> {
                if(f instanceof Square<?>){
                    fixBorderCollisions((Square<?>) f);
                }
            }));
        }catch(ConcurrentModificationException ignored){

        }finally {
            repaint();
        }
    }

    private void fixBorderCollisions(Square<?> square){
        if(square != null){
            if (square.x + Util.SIDELENGTH > size.width || square.x < 0){
                size = new Dimension(size.width + 1, size.height);
                this.setPreferredSize(size);

            }
            if (square.y + Util.SIDELENGTH > size.height || square.y < 0){
                size = new Dimension(size.width, size.height + 1);
                this.setPreferredSize(size);
            }
        }
    }
}