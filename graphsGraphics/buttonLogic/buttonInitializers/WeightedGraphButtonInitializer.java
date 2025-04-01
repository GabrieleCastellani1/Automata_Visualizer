package graphsGraphics.buttonLogic.buttonInitializers;

import graphs.Node;
import graphs.WeightedGraph;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class WeightedGraphButtonInitializer extends ButtonInitializer {
    protected final List<ButtonConfiguration> allComponents;

    public <K> WeightedGraphButtonInitializer(WeightedGraph<K> graph) {

        JButton KruscalButton = new JButton();
        KruscalButton.setMaximumSize(new Dimension(200, 50));
        KruscalButton.setPreferredSize(new Dimension(200, 50));
        KruscalButton.setText("Kruscal");

        GridBagConstraints c1 = new GridBagConstraints();

        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridx = xCoord;
        c1.gridy = yCoord;
        yCoord += 1;

        JButton PrimButton = new JButton();
        PrimButton.setMaximumSize(new Dimension(200, 50));
        PrimButton.setPreferredSize(new Dimension(200, 50));
        PrimButton.setText("Prim");

        GridBagConstraints c2 = new GridBagConstraints();

        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.gridx = xCoord;
        c2.gridy = yCoord;

        JTextField PrimNodeText = new JTextField();
        PrimNodeText.setMaximumSize(new Dimension(50, 50));
        PrimNodeText.setPreferredSize(new Dimension(50, 50));

        GridBagConstraints c3 = new GridBagConstraints();

        c3.fill = GridBagConstraints.HORIZONTAL;
        c3.gridx = xCoord + 1;
        c3.gridy = yCoord;
        yCoord += 1;

        JButton DijkstraButton = new JButton();
        DijkstraButton.setMaximumSize(new Dimension(200, 50));
        DijkstraButton.setPreferredSize(new Dimension(200, 50));
        DijkstraButton.setText("Dijkstra");

        GridBagConstraints c4 = new GridBagConstraints();

        c4.fill = GridBagConstraints.HORIZONTAL;
        c4.gridx = xCoord;
        c4.gridy = yCoord;

        JTextField DijkstraNodeText = new JTextField();
        DijkstraNodeText.setMaximumSize(new Dimension(50, 50));
        DijkstraNodeText.setPreferredSize(new Dimension(50, 50));

        GridBagConstraints c5 = new GridBagConstraints();

        c5.fill = GridBagConstraints.HORIZONTAL;
        c5.gridx = xCoord + 1;
        c5.gridy = yCoord;
        yCoord += 1;

        PrimButton.addActionListener(e -> new Thread(graph::Kruscal).start());

        PrimButton.addActionListener(e -> {
            Optional<Node<K>> optionalNode = graph.findNode((K) PrimNodeText.getText());
            optionalNode.ifPresent(kNode -> new Thread(() -> graph.Prim(kNode)).start());
            PrimNodeText.setText("");
        });

        DijkstraButton.addActionListener(e -> {
            Optional<Node<K>> optionalNode = graph.findNode((K) DijkstraNodeText.getText());
            optionalNode.ifPresent(kNode -> new Thread(() -> graph.Dijkstra(kNode)).start());
            DijkstraNodeText.setText("");
        });

        allComponents = List.of(new ButtonConfiguration(KruscalButton, c1),
                new ButtonConfiguration(PrimButton, c2),
                new ButtonConfiguration(PrimNodeText, c3),
                new ButtonConfiguration(DijkstraButton, c4),
                new ButtonConfiguration(DijkstraNodeText, c5)
        );
    }

    @Override
    public List<ButtonConfiguration> getAllComponents() {
        return allComponents;
    }
}
