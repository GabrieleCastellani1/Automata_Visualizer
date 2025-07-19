package pushDownAutomataGraphics.ViewManager;

import pushDownAutomataGraphics.figures.Square;

import java.util.Stack;
import java.util.Vector;

public class StackViewManager<K> extends AbstractViewManager<K> {
    Stack<Square<K>> squares;

    public StackViewManager() {
        super();
        this.squares = new Stack<>();
    }

    @Override
    public void setCurrentY(int currentY) {
        this.currentY = currentY;
        this.squares.forEach(s -> s.y = currentY);
        this.rectangles.forEach(s -> s.y = currentY-10);
        this.pins.forEach(s -> s.y = currentY);
    }

    public void addSquare(K key) {
        for (Square<K> s : squares){
            s.x += sideLength;
        }
        this.squares.push(new Square<>(key, xStart, currentY, sideLength));
    }

    public void setSquare(int index, K key) {
        Square<K> currentSquare = squares.get(index);
        this.squares.set(index, new Square<>(key, currentSquare.x, currentSquare.y, currentSquare.sideLength));
    }

    public void removeSquare() {
        squares.pop();
        resetSquarePositions();
    }

    public Square<K> get(int i) {
        return this.squares.peek();
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

    public static <K> void printStack(Stack<K> stack) {
        if (stack.isEmpty()) {
            System.out.println("Stack is empty");
            return;
        }

        System.out.println("Stack (top to bottom):");
        for (int i = stack.size() - 1; i >= 0; i--) {
            System.out.println("  " + stack.get(i));
        }
    }
}
