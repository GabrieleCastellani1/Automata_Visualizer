package pushDownAutomataGraphics.ViewManager;

import pushDownAutomataGraphics.figures.Figure;
import pushDownAutomataGraphics.figures.Pin;
import pushDownAutomataGraphics.figures.Rectangle;
import pushDownAutomataGraphics.figures.Square;
import util.Util;

import java.util.Vector;

public abstract class AbstractViewManager<K> {
    final int xStart;
    int currentX;

    public abstract void setCurrentY(int currentY);
    int currentY;
    final int sideLength;
    final Vector<Pin> pins;
    final Vector<Rectangle> rectangles;

    public AbstractViewManager() {
        this.xStart = 50;
        this.currentY = 50;
        this.sideLength = Util.SIDELENGTH;
        this.rectangles = new Vector<>();
        this.pins = new Vector<>();
        currentX = xStart;
    }

    public abstract Square<K> get(int i);
    public abstract Vector<Square<K>> getAll();
    public abstract void addSquare(K key);
    public abstract void removeSquare();

    public Rectangle highlightElement(int i){
        try{
            Square<K> key = get(i);
            Rectangle rect = new Rectangle(
                    key.x - key.sideLength/5,
                    key.y - key.sideLength/5,
                    key.sideLength + key.sideLength/3,
                    key.sideLength + key.sideLength/3
            );
            rectangles.add(rect);
            return rect;
        }catch(ArrayIndexOutOfBoundsException ignored){
            return null;
        }
    }

    public Rectangle highlightArea(int i, int j){
        if(i == j) {
            return highlightElement(i);
        }else{
            try{
                Square<K> key = get(i);
                Rectangle rect = new Rectangle(
                        key.x - key.sideLength/5,
                        key.y - key.sideLength/5,
                        (key.sideLength * (j - i + 1))+ key.sideLength/3,
                        key.sideLength + key.sideLength/3
                );
                rectangles.add(rect);
                return rect;
            }catch(ArrayIndexOutOfBoundsException ignored){
                return null;
            }
        }
    }

    public void removeRect(Rectangle rect){
        rectangles.remove(rect);
    }

    public void removeAllRect(){
        rectangles.removeAll(rectangles);
    }

    public void addPin(int i){
        int x = get(i).x + 30;
        int y = get(i).y;

        Pin pin = new Pin(x, y);
        pins.add(pin);
    }

    public void removeAllPins(){
        pins.removeAll(pins);
    }

    public Vector<Figure> getAllFigures(){
        Vector<Figure> figures = new Vector<>();
        figures.addAll(getAll());
        figures.addAll(rectangles);
        figures.addAll(pins);
        return figures;
    }
}