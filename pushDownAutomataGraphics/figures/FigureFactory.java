package pushDownAutomataGraphics.figures;

public class FigureFactory {

    public Figure createPin(int x, int y){
        return new Pin(x, y);
    }

    public Figure createRectangle(int x, int y, int width, int height){
        return new Rectangle(x, y, width, height);
    }

    public <K extends Comparable<K>> Figure createSquare(K key, int x, int y, int sideLength){
        return new Square<K>(key, x, y, sideLength);
    }
}
