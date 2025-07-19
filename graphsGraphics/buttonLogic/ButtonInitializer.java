package graphsGraphics.buttonLogic;

import graphs.AbstractGraph;

import java.util.List;

public abstract class ButtonInitializer<K, L> {
    protected static int xCoord = 0;
    protected static int yCoord = 0;

    public List<AbstractGraph<K, L>> graphs;
    public ButtonInitializer(List<AbstractGraph<K, L>> graphs){
        this.graphs = graphs;
    }
    public abstract List<ButtonConfiguration> getAllComponents();
}
