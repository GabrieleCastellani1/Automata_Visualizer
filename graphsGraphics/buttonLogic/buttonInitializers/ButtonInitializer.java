package graphsGraphics.buttonLogic.buttonInitializers;

import java.util.List;

public abstract class ButtonInitializer {
    static int xCoord = 0;
    static int yCoord = 0;

    public abstract List<ButtonConfiguration> getAllComponents();
}
