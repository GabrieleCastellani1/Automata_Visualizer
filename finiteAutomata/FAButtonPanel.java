package finiteAutomata;

import graphsGraphics.buttonLogic.ButtonConfiguration;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FAButtonPanel extends JPanel {

    public FAButtonPanel(List<ButtonConfiguration> components) {
        this.setLayout(new GridBagLayout());
        components.forEach(c -> c.component.setVisible(true));
        components.forEach(c -> c.component.setEnabled(true));
        components.forEach(c -> this.add(c.component, c.constraints));
    }
}
