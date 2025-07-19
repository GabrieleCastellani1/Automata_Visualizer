package graphsGraphics.buttonLogic;

import java.awt.*;

public class ButtonConfiguration {
    public Component component;
    public GridBagConstraints constraints;

    public ButtonConfiguration(Component component, GridBagConstraints constraints) {
        this.component = component;
        this.constraints = constraints;
    }
}
