package finiteAutomata.buttons;

import finiteAutomata.FiniteAutomata;
import graphs.AbstractGraph;
import graphs.Node;
import graphsGraphics.buttonLogic.ButtonConfiguration;
import graphsGraphics.buttonLogic.ButtonInitializer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IOButtonInitializer<K, L> extends ButtonInitializer<K, L> {

    public IOButtonInitializer(List<AbstractGraph<K, L>> graphs) {
        super(graphs);
    }

    @Override
    public List<ButtonConfiguration> getAllComponents() {
        return initializeIOButtons(graphs);
    }

    public List<ButtonConfiguration> initializeAddInitialNodeButton(List<AbstractGraph<K, L>> graphs) {

        JButton insertNodeButton = new JButton();
        insertNodeButton.setMaximumSize(new Dimension(200, 50));
        insertNodeButton.setPreferredSize(new Dimension(200, 50));
        insertNodeButton.setText("Inserisci stato iniziale");

        GridBagConstraints c1 = new GridBagConstraints();

        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridx = xCoord;
        c1.gridy = yCoord;

        JTextField insertNodeText = new JTextField();
        insertNodeText.setMaximumSize(new Dimension(50, 50));
        insertNodeText.setPreferredSize(new Dimension(50, 50));

        GridBagConstraints c2 = new GridBagConstraints();

        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.gridx = xCoord + 1;
        c2.gridy = yCoord;
        yCoord += 1;

        insertNodeButton.addActionListener(e -> {
            FiniteAutomata<K,L> automata = (FiniteAutomata<K, L>) graphs.get(0);
            automata.addInitialNode((K) insertNodeText.getText());
            insertNodeText.setText("");
        });

        return List.of(
                new ButtonConfiguration(insertNodeButton, c1),
                new ButtonConfiguration(insertNodeText, c2)
        );
    }

    public List<ButtonConfiguration> initializeAddNodeButton(List<AbstractGraph<K, L>> graphs) {

        JButton insertNodeButton = new JButton();
        insertNodeButton.setMaximumSize(new Dimension(200, 50));
        insertNodeButton.setPreferredSize(new Dimension(200, 50));
        insertNodeButton.setText("Inserisci stato");

        GridBagConstraints c1 = new GridBagConstraints();

        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridx = xCoord;
        c1.gridy = yCoord;

        JTextField insertNodeText = new JTextField();
        insertNodeText.setMaximumSize(new Dimension(50, 50));
        insertNodeText.setPreferredSize(new Dimension(50, 50));

        GridBagConstraints c2 = new GridBagConstraints();

        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.gridx = xCoord + 1;
        c2.gridy = yCoord;
        yCoord += 1;

        insertNodeButton.addActionListener(e -> {
            graphs.get(0).addNode((K) insertNodeText.getText());
            insertNodeText.setText("");
        });

        return List.of(
                new ButtonConfiguration(insertNodeButton, c1),
                new ButtonConfiguration(insertNodeText, c2)
        );
    }

    public List<ButtonConfiguration> initializeAddFinalNodeButton(List<AbstractGraph<K, L>> graphs) {

        JButton insertNodeButton = new JButton();
        insertNodeButton.setMaximumSize(new Dimension(200, 50));
        insertNodeButton.setPreferredSize(new Dimension(200, 50));
        insertNodeButton.setText("Inserisci stato finale");

        GridBagConstraints c1 = new GridBagConstraints();

        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridx = xCoord;
        c1.gridy = yCoord;

        JTextField insertNodeText = new JTextField();
        insertNodeText.setMaximumSize(new Dimension(50, 50));
        insertNodeText.setPreferredSize(new Dimension(50, 50));

        GridBagConstraints c2 = new GridBagConstraints();

        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.gridx = xCoord + 1;
        c2.gridy = yCoord;
        yCoord += 1;

        insertNodeButton.addActionListener(e -> {
            FiniteAutomata<K,L> automata = (FiniteAutomata<K, L>) graphs.get(0);
            automata.addFinalNode((K) insertNodeText.getText());
            insertNodeText.setText("");
        });

        return List.of(
                new ButtonConfiguration(insertNodeButton, c1),
                new ButtonConfiguration(insertNodeText, c2)
        );
    }

    public List<ButtonConfiguration> initializeAddEdgeButton(List<AbstractGraph<K, L>> graphs) {

        JButton addEdgeButton = new JButton();
        addEdgeButton.setMaximumSize(new Dimension(200, 50));
        addEdgeButton.setPreferredSize(new Dimension(200, 50));
        addEdgeButton.setText("Inserisci transizione");

        GridBagConstraints c1 = new GridBagConstraints();

        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridx = xCoord;
        c1.gridy = yCoord;

        JTextField insertFirstNodeText = new JTextField();
        insertFirstNodeText.setMaximumSize(new Dimension(50, 50));
        insertFirstNodeText.setPreferredSize(new Dimension(50, 50));

        GridBagConstraints c2 = new GridBagConstraints();

        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.gridx = xCoord + 1;
        c2.gridy = yCoord;

        JTextField insertSecondNodeText = new JTextField();
        insertSecondNodeText.setMaximumSize(new Dimension(50, 50));
        insertSecondNodeText.setPreferredSize(new Dimension(50, 50));

        GridBagConstraints c3 = new GridBagConstraints();

        c3.fill = GridBagConstraints.HORIZONTAL;
        c3.gridx = xCoord + 2;
        c3.gridy = yCoord;

        JTextField insertWeightText = new JTextField();
        insertWeightText.setMaximumSize(new Dimension(50, 50));
        insertWeightText.setPreferredSize(new Dimension(50, 50));

        GridBagConstraints c4 = new GridBagConstraints();

        c4.fill = GridBagConstraints.HORIZONTAL;
        c4.gridx = xCoord + 3;
        c4.gridy = yCoord;
        yCoord += 1;

        addEdgeButton.addActionListener(e -> {
            graphs.forEach(g -> g.addEdge(
                            (K) insertFirstNodeText.getText(),
                            (K) insertSecondNodeText.getText(),
                            (L) insertWeightText.getText()
                    )
            );
            insertFirstNodeText.setText("");
            insertSecondNodeText.setText("");
            insertWeightText.setText("");
        });

        return List.of(
                new ButtonConfiguration(addEdgeButton, c1),
                new ButtonConfiguration(insertFirstNodeText, c2),
                new ButtonConfiguration(insertSecondNodeText, c3),
                new ButtonConfiguration(insertWeightText, c4)
        );
    }

    public List<ButtonConfiguration> initializeDeleteNodeButton(List<AbstractGraph<K, L>> graphs) {
        JButton deleteNodeButton = new JButton();
        deleteNodeButton.setMaximumSize(new Dimension(200, 50));
        deleteNodeButton.setPreferredSize(new Dimension(200, 50));
        deleteNodeButton.setText("Elimina stato");

        GridBagConstraints c1 = new GridBagConstraints();

        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridx = xCoord;
        c1.gridy = yCoord;

        JTextField deleteNodeText = new JTextField();
        deleteNodeText.setMaximumSize(new Dimension(50, 50));
        deleteNodeText.setPreferredSize(new Dimension(50, 50));

        GridBagConstraints c2 = new GridBagConstraints();

        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.gridx = xCoord + 1;
        c2.gridy = yCoord;
        yCoord += 1;

        deleteNodeButton.addActionListener(e -> {
            Optional<Node<K>> node = graphs.get(0).findNode((K) deleteNodeText.getText());
            node.ifPresent(kNode -> graphs.forEach(g -> g.deleteNode(kNode)));
            deleteNodeText.setText("");
        });

        return List.of(
                new ButtonConfiguration(deleteNodeButton, c1),
                new ButtonConfiguration(deleteNodeText, c2)
        );
    }

    public List<ButtonConfiguration> initializeDeleteEdgeButton(List<AbstractGraph<K, L>> graphs) {

        JButton deleteEdgeButton = new JButton();
        deleteEdgeButton.setMaximumSize(new Dimension(200, 50));
        deleteEdgeButton.setPreferredSize(new Dimension(200, 50));
        deleteEdgeButton.setText("Elimina transizione");

        GridBagConstraints c1 = new GridBagConstraints();

        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridx = xCoord;
        c1.gridy = yCoord;

        JTextField deleteFirstNodeText = new JTextField();
        deleteFirstNodeText.setMaximumSize(new Dimension(50, 50));
        deleteFirstNodeText.setPreferredSize(new Dimension(50, 50));

        GridBagConstraints c2 = new GridBagConstraints();

        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.gridx = xCoord + 1;
        c2.gridy = yCoord;

        JTextField deleteSecondNodeText = new JTextField();
        deleteSecondNodeText.setMaximumSize(new Dimension(50, 50));
        deleteSecondNodeText.setPreferredSize(new Dimension(50, 50));

        GridBagConstraints c3 = new GridBagConstraints();

        c3.fill = GridBagConstraints.HORIZONTAL;
        c3.gridx = xCoord + 2;
        c3.gridy = yCoord;
        yCoord += 1;

        deleteEdgeButton.addActionListener(e -> {
            Optional<Node<K>> node1 = graphs.get(0).findNode((K) deleteFirstNodeText.getText());
            Optional<Node<K>> node2 = graphs.get(0).findNode((K) deleteSecondNodeText.getText());
            if (node1.isPresent() && node2.isPresent()) {
                graphs.forEach(g -> g.deleteEdge(node1.get(), node2.get()));
            }
            deleteFirstNodeText.setText("");
            deleteSecondNodeText.setText("");
        });

        return List.of(
                new ButtonConfiguration(deleteEdgeButton, c1),
                new ButtonConfiguration(deleteFirstNodeText, c2),
                new ButtonConfiguration(deleteSecondNodeText, c3)
        );
    }

    public List<ButtonConfiguration> evaluateStringButton(List<AbstractGraph<K, L>> graphs) {

        JButton insertNodeButton = new JButton();
        insertNodeButton.setMaximumSize(new Dimension(200, 50));
        insertNodeButton.setPreferredSize(new Dimension(200, 50));
        insertNodeButton.setText("Valuta Stringa");

        GridBagConstraints c1 = new GridBagConstraints();

        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridx = xCoord;
        c1.gridy = yCoord;

        JTextField insertNodeText = new JTextField();
        insertNodeText.setMaximumSize(new Dimension(50, 50));
        insertNodeText.setPreferredSize(new Dimension(50, 50));

        GridBagConstraints c2 = new GridBagConstraints();

        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.gridx = xCoord + 1;
        c2.gridy = yCoord;
        yCoord += 1;

        insertNodeButton.addActionListener(e -> {
            String input = insertNodeText.getText();
            insertNodeText.setText("");

            new Thread(() -> {
                FiniteAutomata<K, L> automata = (FiniteAutomata<K, L>) graphs.get(0);
                automata.evaluateString(input);
            }).start();
        });

        return List.of(
                new ButtonConfiguration(insertNodeButton, c1),
                new ButtonConfiguration(insertNodeText, c2)
        );
    }

    public List<ButtonConfiguration> initializeIOButtons(List<AbstractGraph<K, L>> graphs) {
        List<ButtonConfiguration> IOComponents = new ArrayList<>(initializeAddNodeButton(graphs));
        IOComponents.addAll(initializeAddEdgeButton(graphs));
        IOComponents.addAll(initializeDeleteNodeButton(graphs));
        IOComponents.addAll(initializeDeleteEdgeButton(graphs));
        IOComponents.addAll(initializeAddInitialNodeButton(graphs));
        IOComponents.addAll(initializeAddFinalNodeButton(graphs));
        IOComponents.addAll(evaluateStringButton(graphs));
        return IOComponents;
    }
}

