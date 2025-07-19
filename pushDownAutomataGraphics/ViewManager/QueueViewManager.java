package pushDownAutomataGraphics.ViewManager;

import pushDownAutomataGraphics.figures.Square;

import java.util.Vector;

public class QueueViewManager<K> extends AbstractViewManager<K>{
    Vector<Square<K>> squares;

    @Override
    public void setCurrentY(int currentY) {
        this.currentY = currentY;
        this.squares.forEach(s -> s.y = currentY);
    }

    public QueueViewManager() {
        super();
        this.squares = new Vector<>();
    }

    public void addSquare(K key) {
        this.squares.add(new Square<>(key, currentX, currentY, sideLength));
        currentX += sideLength;
    }

    public void setSquare(int index, K key) {
        Square<K> currentSquare = squares.get(index);
        this.squares.set(index, new Square<>(key, currentSquare.x, currentSquare.y, currentSquare.sideLength));
    }

    public void removeSquare() {
        squares.remove(squares.firstElement());
        resetSquarePositions();
    }

    public Square<K> get(int i) {
        return this.squares.elementAt(i);
    }

    void resetSquarePositions() {
        currentX = xStart;
        squares.forEach(s -> {
            s.x = currentX;
            currentX += sideLength;
        });
    }

    public Vector<Square<K>> getAll() {
        return squares;
    }
}
