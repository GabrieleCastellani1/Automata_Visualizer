package graphsGraphics.buttonLogic.buttonInitializers;

import graphs.OrientedGraph;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OrientedGraphButtonInitializer extends ButtonInitializer {

    protected final List<ButtonConfiguration> allComponents;

    public OrientedGraphButtonInitializer(OrientedGraph<?> graph) {

        JButton KosarajuButton = new JButton();
        KosarajuButton.setMaximumSize(new Dimension(200, 50));
        KosarajuButton.setPreferredSize(new Dimension(200, 50));
        KosarajuButton.setText("Kosaraju");

        GridBagConstraints c1 = new GridBagConstraints();

        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridx = xCoord;
        c1.gridy = yCoord;
        yCoord += 1;

        KosarajuButton.addActionListener(e -> new Thread(graph::Kosaraju).start());

        allComponents = List.of(new ButtonConfiguration(KosarajuButton, c1));
    }

    @Override
    public List<ButtonConfiguration> getAllComponents() {
        return allComponents;
    }
}
