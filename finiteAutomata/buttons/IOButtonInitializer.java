package finiteAutomata.buttons;

import finiteAutomata.FiniteAutomata;
import graphs.AbstractGraph;
import graphs.Node;
import graphsGraphics.buttonLogic.ButtonConfiguration;
import graphsGraphics.buttonLogic.ButtonInitializer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IOButtonInitializer<K, L> extends ButtonInitializer<K, L> {

    // Modern color palette
    private static final Color PRIMARY_BUTTON_COLOR = new Color(0, 123, 255);
    private static final Color SUCCESS_BUTTON_COLOR = new Color(40, 167, 69);
    private static final Color DANGER_BUTTON_COLOR = new Color(220, 53, 69);
    private static final Color WARNING_BUTTON_COLOR = new Color(255, 193, 7);
    private static final Color INFO_BUTTON_COLOR = new Color(23, 162, 184);

    private static final Font MODERN_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font MODERN_FONT_BOLD = new Font("Segoe UI", Font.BOLD, 12);

    public IOButtonInitializer(List<AbstractGraph<K, L>> graphs) {
        super(graphs);
    }

    @Override
    public List<ButtonConfiguration> getAllComponents() {
        return initializeIOButtons(graphs);
    }

    public List<ButtonConfiguration> initializeAddInitialNodeButton(List<AbstractGraph<K, L>> graphs) {
        JButton insertNodeButton = createModernButton("Add Initial State", SUCCESS_BUTTON_COLOR);

        GridBagConstraints c1 = createConstraints(xCoord, yCoord);

        JTextField insertNodeText = createModernTextField("Enter state name...");

        GridBagConstraints c2 = createConstraints(xCoord + 1, yCoord);
        yCoord += 1;

        insertNodeButton.addActionListener(e -> {
            String text = insertNodeText.getText().trim();
            if (!text.isEmpty() && !text.equals("Enter state name...")) {
                FiniteAutomata<K,L> automata = (FiniteAutomata<K, L>) graphs.get(0);
                automata.addInitialNode((K) text);
                insertNodeText.setText("");
                showSuccessMessage("Initial state '" + text + "' added successfully!");
            } else {
                showErrorMessage("Please enter a valid state name.");
            }
        });

        return List.of(
                new ButtonConfiguration(insertNodeButton, c1),
                new ButtonConfiguration(insertNodeText, c2)
        );
    }

    public List<ButtonConfiguration> initializeAddNodeButton(List<AbstractGraph<K, L>> graphs) {
        JButton insertNodeButton = createModernButton("Add State", PRIMARY_BUTTON_COLOR);

        GridBagConstraints c1 = createConstraints(xCoord, yCoord);

        JTextField insertNodeText = createModernTextField("Enter state name...");

        GridBagConstraints c2 = createConstraints(xCoord + 1, yCoord);
        yCoord += 1;

        insertNodeButton.addActionListener(e -> {
            String text = insertNodeText.getText().trim();
            if (!text.isEmpty() && !text.equals("Enter state name...")) {
                graphs.get(0).addNode((K) text);
                insertNodeText.setText("");
                showSuccessMessage("State '" + text + "' added successfully!");
            } else {
                showErrorMessage("Please enter a valid state name.");
            }
        });

        return List.of(
                new ButtonConfiguration(insertNodeButton, c1),
                new ButtonConfiguration(insertNodeText, c2)
        );
    }

    public List<ButtonConfiguration> initializeAddFinalNodeButton(List<AbstractGraph<K, L>> graphs) {
        JButton insertNodeButton = createModernButton("Add Final State", INFO_BUTTON_COLOR);

        GridBagConstraints c1 = createConstraints(xCoord, yCoord);

        JTextField insertNodeText = createModernTextField("Enter state name...");

        GridBagConstraints c2 = createConstraints(xCoord + 1, yCoord);
        yCoord += 1;

        insertNodeButton.addActionListener(e -> {
            String text = insertNodeText.getText().trim();
            if (!text.isEmpty() && !text.equals("Enter state name...")) {
                FiniteAutomata<K,L> automata = (FiniteAutomata<K, L>) graphs.get(0);
                automata.addFinalNode((K) text);
                insertNodeText.setText("");
                showSuccessMessage("Final state '" + text + "' added successfully!");
            } else {
                showErrorMessage("Please enter a valid state name.");
            }
        });

        return List.of(
                new ButtonConfiguration(insertNodeButton, c1),
                new ButtonConfiguration(insertNodeText, c2)
        );
    }

    public List<ButtonConfiguration> initializeAddEdgeButton(List<AbstractGraph<K, L>> graphs) {
        JButton addEdgeButton = createModernButton("Add Transition", PRIMARY_BUTTON_COLOR);

        GridBagConstraints c1 = createConstraints(xCoord, yCoord);

        JTextField insertFirstNodeText = createModernTextField("From state...");
        GridBagConstraints c2 = createConstraints(xCoord + 1, yCoord);

        JTextField insertSecondNodeText = createModernTextField("To state...");
        GridBagConstraints c3 = createConstraints(xCoord + 2, yCoord);

        JTextField insertWeightText = createModernTextField("Symbol...");
        GridBagConstraints c4 = createConstraints(xCoord + 3, yCoord);
        yCoord += 1;

        addEdgeButton.addActionListener(e -> {
            String from = insertFirstNodeText.getText().trim();
            String to = insertSecondNodeText.getText().trim();
            String symbol = insertWeightText.getText().trim();

            if (!from.isEmpty() && !to.isEmpty() && !symbol.isEmpty() &&
                    !from.equals("From state...") && !to.equals("To state...") && !symbol.equals("Symbol...")) {
                graphs.forEach(g -> g.addEdge((K) from, (K) to, (L) symbol));
                insertFirstNodeText.setText("");
                insertSecondNodeText.setText("");
                insertWeightText.setText("");
                showSuccessMessage("Transition added: " + from + " → " + to + " (" + symbol + ")");
            } else {
                showErrorMessage("Please fill in all fields for the transition.");
            }
        });

        return List.of(
                new ButtonConfiguration(addEdgeButton, c1),
                new ButtonConfiguration(insertFirstNodeText, c2),
                new ButtonConfiguration(insertSecondNodeText, c3),
                new ButtonConfiguration(insertWeightText, c4)
        );
    }

    public List<ButtonConfiguration> initializeDeleteNodeButton(List<AbstractGraph<K, L>> graphs) {
        JButton deleteNodeButton = createModernButton("Delete State", DANGER_BUTTON_COLOR);

        GridBagConstraints c1 = createConstraints(xCoord, yCoord);

        JTextField deleteNodeText = createModernTextField("State to delete...");

        GridBagConstraints c2 = createConstraints(xCoord + 1, yCoord);
        yCoord += 1;

        deleteNodeButton.addActionListener(e -> {
            String text = deleteNodeText.getText().trim();
            if (!text.isEmpty() && !text.equals("State to delete...")) {
                Optional<Node<K>> node = graphs.get(0).findNode((K) text);
                if (node.isPresent()) {
                    graphs.forEach(g -> g.deleteNode(node.get()));
                    deleteNodeText.setText("");
                    showSuccessMessage("State '" + text + "' deleted successfully!");
                } else {
                    showErrorMessage("State '" + text + "' not found.");
                }
            } else {
                showErrorMessage("Please enter a valid state name to delete.");
            }
        });

        return List.of(
                new ButtonConfiguration(deleteNodeButton, c1),
                new ButtonConfiguration(deleteNodeText, c2)
        );
    }

    public List<ButtonConfiguration> initializeDeleteEdgeButton(List<AbstractGraph<K, L>> graphs) {
        JButton deleteEdgeButton = createModernButton("Delete Transition", DANGER_BUTTON_COLOR);

        GridBagConstraints c1 = createConstraints(xCoord, yCoord);

        JTextField deleteFirstNodeText = createModernTextField("From state...");
        GridBagConstraints c2 = createConstraints(xCoord + 1, yCoord);

        JTextField deleteSecondNodeText = createModernTextField("To state...");
        GridBagConstraints c3 = createConstraints(xCoord + 2, yCoord);
        yCoord += 1;

        deleteEdgeButton.addActionListener(e -> {
            String from = deleteFirstNodeText.getText().trim();
            String to = deleteSecondNodeText.getText().trim();

            if (!from.isEmpty() && !to.isEmpty() &&
                    !from.equals("From state...") && !to.equals("To state...")) {
                Optional<Node<K>> node1 = graphs.get(0).findNode((K) from);
                Optional<Node<K>> node2 = graphs.get(0).findNode((K) to);

                if (node1.isPresent() && node2.isPresent()) {
                    graphs.forEach(g -> g.deleteEdge(node1.get(), node2.get()));
                    deleteFirstNodeText.setText("");
                    deleteSecondNodeText.setText("");
                    showSuccessMessage("Transition deleted: " + from + " → " + to);
                } else {
                    showErrorMessage("One or both states not found.");
                }
            } else {
                showErrorMessage("Please enter both states for the transition to delete.");
            }
        });

        return List.of(
                new ButtonConfiguration(deleteEdgeButton, c1),
                new ButtonConfiguration(deleteFirstNodeText, c2),
                new ButtonConfiguration(deleteSecondNodeText, c3)
        );
    }

    public List<ButtonConfiguration> evaluateStringButton(List<AbstractGraph<K, L>> graphs) {
        JButton evaluateButton = createModernButton("Evaluate String", WARNING_BUTTON_COLOR);
        evaluateButton.setForeground(Color.BLACK); // Better contrast for warning color

        GridBagConstraints c1 = createConstraints(xCoord, yCoord);

        JTextField inputText = createModernTextField("String to evaluate...");

        GridBagConstraints c2 = createConstraints(xCoord + 1, yCoord);
        yCoord += 1;

        evaluateButton.addActionListener(e -> {
            String input = inputText.getText().trim();
            if (!input.isEmpty() && !input.equals("String to evaluate...")) {
                inputText.setText("");

                // Show processing indicator
                evaluateButton.setEnabled(false);
                evaluateButton.setText("Processing...");

                new Thread(() -> {
                    try {
                        FiniteAutomata<K, L> automata = (FiniteAutomata<K, L>) graphs.get(0);
                        automata.evaluateString(input);

                        SwingUtilities.invokeLater(() -> {
                            evaluateButton.setEnabled(true);
                            evaluateButton.setText("Evaluate String");
                            showInfoMessage("String '" + input + "' evaluation completed. Check the graph for results.");
                        });
                    } catch (Exception ex) {
                        SwingUtilities.invokeLater(() -> {
                            evaluateButton.setEnabled(true);
                            evaluateButton.setText("Evaluate String");
                            showErrorMessage("Error evaluating string: " + ex.getMessage());
                        });
                    }
                }).start();
            } else {
                showErrorMessage("Please enter a string to evaluate.");
            }
        });

        return List.of(
                new ButtonConfiguration(evaluateButton, c1),
                new ButtonConfiguration(inputText, c2)
        );
    }

    public List<ButtonConfiguration> initializeIOButtons(List<AbstractGraph<K, L>> graphs) {
        List<ButtonConfiguration> IOComponents = new ArrayList<>();

        // Add section headers for better organization
        IOComponents.addAll(createSectionHeader("State Management"));
        IOComponents.addAll(initializeAddNodeButton(graphs));
        IOComponents.addAll(initializeAddInitialNodeButton(graphs));
        IOComponents.addAll(initializeAddFinalNodeButton(graphs));
        IOComponents.addAll(initializeDeleteNodeButton(graphs));

        IOComponents.addAll(createSectionHeader("Transition Management"));
        IOComponents.addAll(initializeAddEdgeButton(graphs));
        IOComponents.addAll(initializeDeleteEdgeButton(graphs));

        IOComponents.addAll(createSectionHeader("String Evaluation"));
        IOComponents.addAll(evaluateStringButton(graphs));

        return IOComponents;
    }

    // Helper methods for creating modern UI components

    private JButton createModernButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(200, 40));
        button.setPreferredSize(new Dimension(200, 40));
        button.setMinimumSize(new Dimension(200, 40));

        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setFont(MODERN_FONT_BOLD);

        // Rounded corners effect
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(backgroundColor.darker(), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));

        // Hover effects
        Color hoverColor = backgroundColor.darker();
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(hoverColor);
                    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(backgroundColor);
                    button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });

        return button;
    }

    private JTextField createModernTextField(String placeholder) {
        JTextField textField = new JTextField(placeholder);
        textField.setMaximumSize(new Dimension(150, 40));
        textField.setPreferredSize(new Dimension(150, 40));
        textField.setMinimumSize(new Dimension(150, 40));

        textField.setBackground(Color.WHITE);
        textField.setForeground(new Color(73, 80, 87));
        textField.setFont(MODERN_FONT);

        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        // Placeholder behavior
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(new Color(33, 37, 41));
                }
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_BUTTON_COLOR, 2),
                        BorderFactory.createEmptyBorder(7, 11, 7, 11)
                ));
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(new Color(73, 80, 87));
                }
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });

        return textField;
    }

    private GridBagConstraints createConstraints(int x, int y) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.insets = new Insets(5, 5, 5, 5);
        return constraints;
    }

    private List<ButtonConfiguration> createSectionHeader(String title) {
        JLabel headerLabel = new JLabel(title);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerLabel.setForeground(new Color(52, 58, 64));
        headerLabel.setBorder(new EmptyBorder(15, 5, 5, 5));

        GridBagConstraints headerConstraints = new GridBagConstraints();
        headerConstraints.fill = GridBagConstraints.HORIZONTAL;
        headerConstraints.gridx = xCoord;
        headerConstraints.gridy = yCoord;
        headerConstraints.gridwidth = 4; // Span across all columns
        headerConstraints.insets = new Insets(15, 0, 5, 0);
        yCoord += 1;

        return List.of(new ButtonConfiguration(headerLabel, headerConstraints));
    }

    private void showSuccessMessage(String message) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog("Success");
        styleDialog(dialog);
        dialog.setVisible(true);
    }

    private void showErrorMessage(String message) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
        JDialog dialog = optionPane.createDialog("Error");
        styleDialog(dialog);
        dialog.setVisible(true);
    }

    private void showInfoMessage(String message) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog("Information");
        styleDialog(dialog);
        dialog.setVisible(true);
    }

    private void styleDialog(JDialog dialog) {
        dialog.setFont(MODERN_FONT);
        dialog.getContentPane().setBackground(Color.WHITE);
    }
}