package graphsGraphics;

import graphsGraphics.buttonLogic.buttonInitializers.ButtonConfiguration;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GraphButtonPanel extends JPanel {

    public GraphButtonPanel(List<ButtonConfiguration> components) {
        this.setLayout(new GridBagLayout());
        components.forEach(c -> c.component.setVisible(true));
        components.forEach(c -> c.component.setEnabled(true));
        components.forEach(c -> this.add(c.component, c.constraints));
    }
}
