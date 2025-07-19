package pushDownAutomataGraphics;

import pushDownAutomata.PushDownAutomata;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class PDATransitionGUI<K> extends JFrame {
    private PushDownAutomata<K> pda;
    private JTable transitionTable;
    private DefaultTableModel tableModel;
    private JSplitPane splitPane;

    // Input fields
    private JTextField fromStateField;
    private JTextField inputSymbolField;
    private JTextField stackTopField;
    private JTextField toStateField;
    private JTextField pushSymbolsField;

    public PDATransitionGUI(PushDownAutomata<K> pda) {
        this.pda = pda;
        initializeGUI();
        this.setVisible(true);
    }

    private void initializeGUI() {
        setTitle("PDA Transition Matrix Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Create the split pane
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300);

        // Create left panel with controls
        JPanel controlPanel = createControlPanel();
        JScrollPane controlScrollPane = new JScrollPane(controlPanel);
        controlScrollPane.setPreferredSize(new Dimension(300, 0));

        // Create right panel with table
        JPanel tablePanel = createTablePanel();
        JScrollPane tableScrollPane = new JScrollPane(tablePanel);

        splitPane.setLeftComponent(controlScrollPane);
        splitPane.setRightComponent(tableScrollPane);

        add(splitPane);

        // Initial table update
        updateTable();
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(new JLabel("Add Transition"), gbc);

        gbc.gridwidth = 1;

        // From State
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("From State:"), gbc);
        gbc.gridx = 1;
        fromStateField = new JTextField(10);
        panel.add(fromStateField, gbc);

        // Input Symbol
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Input Symbol:"), gbc);
        gbc.gridx = 1;
        inputSymbolField = new JTextField(10);
        panel.add(inputSymbolField, gbc);

        // Stack Top
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Stack Top:"), gbc);
        gbc.gridx = 1;
        stackTopField = new JTextField(10);
        panel.add(stackTopField, gbc);

        // To State
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("To State:"), gbc);
        gbc.gridx = 1;
        toStateField = new JTextField(10);
        panel.add(toStateField, gbc);

        // Push Symbols
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Push Symbols:"), gbc);
        gbc.gridx = 1;
        pushSymbolsField = new JTextField(10);
        panel.add(pushSymbolsField, gbc);

        // Help text
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        JLabel helpLabel = new JLabel("<html><small>Push Symbols: comma-separated<br>Leave empty for epsilon/pop only</small></html>");
        panel.add(helpLabel, gbc);

        // Add Transition Button
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton addButton = new JButton("Add Transition");
        addButton.addActionListener(new AddTransitionListener());
        panel.add(addButton, gbc);

        // Add Epsilon Transition Button
        gbc.gridy = 8;
        JButton addEpsilonButton = new JButton("Add Epsilon Transition");
        addEpsilonButton.addActionListener(new AddEpsilonTransitionListener());
        panel.add(addEpsilonButton, gbc);

        // Clear Button
        gbc.gridy = 9;
        JButton clearButton = new JButton("Clear Fields");
        clearButton.addActionListener(e -> clearFields());
        panel.add(clearButton, gbc);

        // Delete Transition Button
        gbc.gridy = 10;
        JButton deleteButton = new JButton("Delete Transition");
        deleteButton.addActionListener(new DeleteTransitionListener());
        panel.add(deleteButton, gbc);

        // Refresh Button
        gbc.gridy = 11;
        JButton refreshButton = new JButton("Refresh Table");
        refreshButton.addActionListener(e -> updateTable());
        panel.add(refreshButton, gbc);

        // Create New Panel Button
        gbc.gridy = 12;
        JButton newPanelButton = new JButton("Create Simulation");
        newPanelButton.addActionListener(e -> createNewPanel());
        panel.add(newPanelButton, gbc);

        // Setup section
        gbc.gridy = 13;
        panel.add(new JLabel("Setup:"), gbc);

        // Initial State
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 14;
        panel.add(new JLabel("Initial State:"), gbc);
        gbc.gridx = 1;
        JTextField initialStateField = new JTextField(10);
        panel.add(initialStateField, gbc);

        gbc.gridx = 0; gbc.gridy = 15; gbc.gridwidth = 2;
        JButton setInitialButton = new JButton("Set Initial State");
        setInitialButton.addActionListener(e -> {
            try {
                int state = Integer.parseInt(initialStateField.getText());
                pda.setInitialState(state);
                JOptionPane.showMessageDialog(this, "Initial state set to: " + state);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid integer");
            }
        });
        panel.add(setInitialButton, gbc);

        // Accept State section
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 16;
        panel.add(new JLabel("Accept State:"), gbc);
        gbc.gridx = 1;
        JTextField acceptStateField = new JTextField(10);
        panel.add(acceptStateField, gbc);

        gbc.gridx = 0; gbc.gridy = 17; gbc.gridwidth = 2;
        JButton addAcceptStateButton = new JButton("Add Accept State");
        addAcceptStateButton.addActionListener(e -> {
            try {
                int state = Integer.parseInt(acceptStateField.getText());
                pda.addAcceptState(state);
                acceptStateField.setText(""); // Clear the field after adding
                JOptionPane.showMessageDialog(this, "Accept state added: " + state);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid integer");
            }
        });
        panel.add(addAcceptStateButton, gbc);

        // Display current accept states
        gbc.gridy = 18;
        JButton showAcceptStatesButton = new JButton("Show Accept States");
        showAcceptStatesButton.addActionListener(e -> {
            Set<Integer> acceptStates = pda.getAcceptStates();
            if (acceptStates.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No accept states defined");
            } else {
                JOptionPane.showMessageDialog(this, "Accept states: " + acceptStates);
            }
        });
        panel.add(showAcceptStatesButton, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Create table
        String[] columns = {"From State", "Input", "Stack Top", "To State", "Push Symbols"};
        tableModel = new DefaultTableModel(columns, 0);
        transitionTable = new JTable(tableModel);

        // Make table read-only
        transitionTable.setDefaultEditor(Object.class, null);

        JScrollPane scrollPane = new JScrollPane(transitionTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add title
        panel.add(new JLabel("Transition Matrix", SwingConstants.CENTER), BorderLayout.NORTH);

        return panel;
    }

    private void createNewPanel() {
        JFrame newFrame = new PDAFrame<>(this.pda);
        newFrame.setLocationRelativeTo(this);
        newFrame.setVisible(true);
    }

    private void updateTable() {
        tableModel.setRowCount(0);

        Map<PushDownAutomata.TransitionKey<K>, List<PushDownAutomata.TransitionValue<K>>> matrix =
                pda.getTransitionMatrix();

        for (Map.Entry<PushDownAutomata.TransitionKey<K>, List<PushDownAutomata.TransitionValue<K>>> entry : matrix.entrySet()) {
            PushDownAutomata.TransitionKey<K> key = entry.getKey();
            List<PushDownAutomata.TransitionValue<K>> values = entry.getValue();

            for (PushDownAutomata.TransitionValue<K> value : values) {
                String pushSymbolsStr;
                if (value.pushSymbols.isEmpty()) {
                    pushSymbolsStr = "pop";
                } else {
                    List<K> reversed = new ArrayList<>(value.pushSymbols);
                    Collections.reverse(reversed);
                    pushSymbolsStr = reversed.toString();
                }
                Object[] row = {
                        key.state,
                        key.input == null ? "ε" : key.input.toString(),
                        key.stackTop == null ? "ε" : key.stackTop.toString(),
                        value.newState,
                        value.pushSymbols.isEmpty() ? "pop" : pushSymbolsStr
                };
                tableModel.addRow(row);
            }
        }
    }

    private void clearFields() {
        fromStateField.setText("");
        inputSymbolField.setText("");
        stackTopField.setText("");
        toStateField.setText("");
        pushSymbolsField.setText("");
    }

    @SuppressWarnings("unchecked")
    private K parseSymbol(String text) {
        if (text.trim().isEmpty()) return null;
        // Simple string parsing - adjust based on your K type
        return (K) text.trim();
    }

    private List<K> parsePushSymbols(String text) {
        List<K> symbols = new ArrayList<>();
        if (text.trim().isEmpty()) return symbols;

        String[] parts = text.split(",");
        for (String part : parts) {
            K symbol = parseSymbol(part);
            if (symbol != null) {
                symbols.add(symbol);
            }
        }
        return symbols;
    }

    private class AddTransitionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int fromState = Integer.parseInt(fromStateField.getText());
                K inputSymbol = parseSymbol(inputSymbolField.getText());
                K stackTop = parseSymbol(stackTopField.getText());
                int toState = Integer.parseInt(toStateField.getText());
                List<K> pushSymbols = parsePushSymbols(pushSymbolsField.getText());
                Collections.reverse(pushSymbols);

                pda.addTransition(fromState, inputSymbol, stackTop, toState, pushSymbols);
                updateTable();
                clearFields();

                JOptionPane.showMessageDialog(PDATransitionGUI.this, "Transition added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(PDATransitionGUI.this, "Please enter valid integers for states");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PDATransitionGUI.this, "Error adding transition: " + ex.getMessage());
            }
        }
    }

    private class AddEpsilonTransitionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int fromState = Integer.parseInt(fromStateField.getText());
                K stackTop = parseSymbol(stackTopField.getText());
                int toState = Integer.parseInt(toStateField.getText());
                List<K> pushSymbols = parsePushSymbols(pushSymbolsField.getText());
                Collections.reverse(pushSymbols);

                pda.addEpsilonTransition(fromState, stackTop, toState, pushSymbols);
                updateTable();
                clearFields();

                JOptionPane.showMessageDialog(PDATransitionGUI.this, "Epsilon transition added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(PDATransitionGUI.this, "Please enter valid integers for states");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PDATransitionGUI.this, "Error adding epsilon transition: " + ex.getMessage());
            }
        }
    }

    private class DeleteTransitionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int fromState = Integer.parseInt(fromStateField.getText());
                K inputSymbol = parseSymbol(inputSymbolField.getText());
                K stackTop = parseSymbol(stackTopField.getText());
                int toState = Integer.parseInt(toStateField.getText());
                List<K> pushSymbols = parsePushSymbols(pushSymbolsField.getText());

                PushDownAutomata.TransitionKey<K> key = new PushDownAutomata.TransitionKey<>(fromState, inputSymbol, stackTop);
                PushDownAutomata.TransitionValue<K> valueToRemove = new PushDownAutomata.TransitionValue<>(toState, pushSymbols);

                Map<PushDownAutomata.TransitionKey<K>, List<PushDownAutomata.TransitionValue<K>>> matrix =
                        pda.getTransitionMatrix();

                List<PushDownAutomata.TransitionValue<K>> transitions = matrix.get(key);
                if (transitions != null) {
                    boolean removed = transitions.removeIf(transition ->
                            transition.newState == valueToRemove.newState &&
                                    transition.pushSymbols.equals(valueToRemove.pushSymbols));

                    if (removed) {
                        // Remove the key entirely if no transitions remain
                        if (transitions.isEmpty()) {
                            matrix.remove(key);
                        }
                        updateTable();
                        clearFields();
                        JOptionPane.showMessageDialog(PDATransitionGUI.this, "Transition deleted successfully!");
                    } else {
                        JOptionPane.showMessageDialog(PDATransitionGUI.this, "Transition not found!");
                    }
                } else {
                    JOptionPane.showMessageDialog(PDATransitionGUI.this, "Transition not found!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(PDATransitionGUI.this, "Please enter valid integers for states");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PDATransitionGUI.this, "Error deleting transition: " + ex.getMessage());
            }
        }
    }

    // Example usage
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PushDownAutomata<String> pda = new PushDownAutomata<>();
            PDATransitionGUI<String> gui = new PDATransitionGUI<>(pda);
            gui.setVisible(true);
        });
    }
}