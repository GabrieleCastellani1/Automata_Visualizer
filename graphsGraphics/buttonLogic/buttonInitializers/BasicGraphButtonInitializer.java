package graphsGraphics.buttonLogic.buttonInitializers;

import graphs.AbstractGraph;
import graphs.Node;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class BasicGraphButtonInitializer extends ButtonInitializer {
    protected final List<ButtonConfiguration> allComponents;

    public <K> BasicGraphButtonInitializer(List<AbstractGraph<K>> graphs) {

        JButton BFSButton = new JButton();
        BFSButton.setMaximumSize(new Dimension(200, 50));
        BFSButton.setPreferredSize(new Dimension(200, 50));
        BFSButton.setText("BFS");

        GridBagConstraints c1 = new GridBagConstraints();

        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridx = xCoord;
        c1.gridy = yCoord;

        JTextField BFSNodeText = new JTextField();
        BFSNodeText.setMaximumSize(new Dimension(50, 50));
        BFSNodeText.setPreferredSize(new Dimension(50, 50));

        GridBagConstraints c2 = new GridBagConstraints();

        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.gridx = xCoord + 1;
        c2.gridy = yCoord;
        yCoord += 1;

        JButton DFSButton = new JButton();
        DFSButton.setMaximumSize(new Dimension(200, 50));
        DFSButton.setPreferredSize(new Dimension(200, 50));
        DFSButton.setText("DFS");

        BFSButton.addActionListener(e -> {
            Optional<Node<K>> optionalNode = graphs.get(0).findNode((K) BFSNodeText.getText());
            BFSNodeText.setText("");
            new Thread(() -> optionalNode.ifPresent(graphs.get(0)::BFS)).start();
        });

        GridBagConstraints c3 = new GridBagConstraints();

        c3.fill = GridBagConstraints.HORIZONTAL;
        c3.gridx = xCoord;
        c3.gridy = yCoord;
        yCoord += 1;

        DFSButton.addActionListener(e -> {
            new Thread(() -> graphs.get(0).DFS()).start();
        });

        allComponents = List.of(
                new ButtonConfiguration(BFSButton, c1),
                new ButtonConfiguration(BFSNodeText, c2),
                new ButtonConfiguration(DFSButton, c3)
        );
    }

    public List<ButtonConfiguration> getAllComponents() {
        return allComponents;
    }
}
