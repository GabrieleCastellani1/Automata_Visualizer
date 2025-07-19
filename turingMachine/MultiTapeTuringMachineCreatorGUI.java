package turingMachine;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class MultiTapeTuringMachineCreatorGUI extends JFrame {
    private MultiTapeTuringMachineLogic machine;
    private JTextField[] inputFields;
    private JLabel stateLabel;
    private JLabel stepLabel;
    private JLabel statusLabel;
    private JPanel[] tapePanels;
    private JButton loadButton;
    private JButton stepButton;
    private JButton runButton;
    private JButton pauseButton;
    private JButton resetButton;
    private Timer animationTimer;

    // Machine configuration components
    private JTextField statesField;
    private JTextField alphabetField;
    private JTextField tapeAlphabetField;
    private JTextField initialStateField;
    private JTextField acceptStatesField;
    private JTextField rejectStatesField;
    private JTextField blankSymbolField;
    private JSpinner numTapesSpinner;

    // Transition creation components
    private JTextField currentStateField;
    private JTextField[] readSymbolFields;
    private JTextField nextStateField;
    private JTextField[] writeSymbolFields;
    private JComboBox<MultiTapeTransition.Direction>[] directionCombos;
    private JButton addTransitionButton;
    private JButton removeTransitionButton;
    private JButton clearTransitionsButton;
    private JTable transitionTable;
    private DefaultTableModel transitionTableModel;
    private JPanel transitionInputPanel;

    // Machine creation button
    private JButton createMachineButton;
    private int currentNumTapes = 2;

    // Simulation panel components that need to be rebuilt
    private JPanel inputPanel;
    private JPanel tapesContainer;
    private JScrollPane tapesScrollPane;

    public MultiTapeTuringMachineCreatorGUI() {
        initializeGUI();
        setupEventListeners();
        createMachine();
    }

    private void initializeGUI() {
        setTitle("Multi-Tape Turing Machine Creator & Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Machine Configuration Tab
        JPanel configPanel = createConfigurationPanel();
        tabbedPane.addTab("Configuration", configPanel);

        // Simulation Tab
        JPanel simulationPanel = createSimulationPanel();
        tabbedPane.addTab("Simulation", simulationPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Animation timer
        animationTimer = new Timer(500, e -> {
            if (machine != null && machine.isRunning()) {
                if (!machine.step()) {
                    animationTimer.stop();
                }
                updateDisplay();
            } else {
                animationTimer.stop();
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createConfigurationPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Machine parameters panel
        JPanel paramsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Number of tapes
        gbc.gridx = 0; gbc.gridy = 0;
        paramsPanel.add(new JLabel("Number of Tapes:"), gbc);
        gbc.gridx = 1;
        numTapesSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
        numTapesSpinner.addChangeListener(e -> {
            updateTransitionInputPanel();
            rebuildSimulationInputPanel();
            createMachine(); // Add this line to auto-create machine when tapes change
        });
        paramsPanel.add(numTapesSpinner, gbc);

        // Machine parameters
        gbc.gridx = 0; gbc.gridy = 1;
        paramsPanel.add(new JLabel("States (comma-separated):"), gbc);
        gbc.gridx = 1;
        statesField = new JTextField("q0,q1,q2,qaccept", 20);
        paramsPanel.add(statesField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        paramsPanel.add(new JLabel("Input Alphabet:"), gbc);
        gbc.gridx = 1;
        alphabetField = new JTextField("0,1", 20);
        paramsPanel.add(alphabetField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        paramsPanel.add(new JLabel("Tape Alphabet:"), gbc);
        gbc.gridx = 1;
        tapeAlphabetField = new JTextField("0,1,_", 20);
        paramsPanel.add(tapeAlphabetField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        paramsPanel.add(new JLabel("Initial State:"), gbc);
        gbc.gridx = 1;
        initialStateField = new JTextField("q0", 20);
        paramsPanel.add(initialStateField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        paramsPanel.add(new JLabel("Accept States:"), gbc);
        gbc.gridx = 1;
        acceptStatesField = new JTextField("qaccept", 20);
        paramsPanel.add(acceptStatesField, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        paramsPanel.add(new JLabel("Reject States:"), gbc);
        gbc.gridx = 1;
        rejectStatesField = new JTextField("", 20);
        paramsPanel.add(rejectStatesField, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        paramsPanel.add(new JLabel("Blank Symbol:"), gbc);
        gbc.gridx = 1;
        blankSymbolField = new JTextField("_", 20);
        paramsPanel.add(blankSymbolField, gbc);

        // Transition creation panel
        JPanel transitionPanel = new JPanel(new BorderLayout());
        transitionPanel.setBorder(BorderFactory.createTitledBorder("Transitions"));

        // Create initial transition input panel
        createTransitionInputPanel();

        // Transition table
        createTransitionTable();
        JScrollPane tableScrollPane = new JScrollPane(transitionTable);
        tableScrollPane.setPreferredSize(new Dimension(800, 200));

        // Button panel for transition management
        JPanel buttonPanel = new JPanel(new FlowLayout());
        removeTransitionButton = new JButton("Remove Selected");
        clearTransitionsButton = new JButton("Clear All");
        buttonPanel.add(removeTransitionButton);
        buttonPanel.add(clearTransitionsButton);

        transitionPanel.add(transitionInputPanel, BorderLayout.NORTH);
        transitionPanel.add(tableScrollPane, BorderLayout.CENTER);
        transitionPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Create machine button
        JPanel createPanel = new JPanel(new FlowLayout());
        createMachineButton = new JButton("Create Machine");
        createMachineButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        createPanel.add(createMachineButton);

        // Layout
        panel.add(paramsPanel, BorderLayout.NORTH);
        panel.add(transitionPanel, BorderLayout.CENTER);
        panel.add(createPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void createTransitionInputPanel() {
        transitionInputPanel = new JPanel(new GridBagLayout());
        updateTransitionInputPanel();
    }

    private void updateTransitionInputPanel() {
        int newNumTapes = (Integer) numTapesSpinner.getValue();
        if (newNumTapes == currentNumTapes && readSymbolFields != null) {
            return;
        }

        currentNumTapes = newNumTapes;
        transitionInputPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Current state and next state
        gbc.gridx = 0; gbc.gridy = 0;
        transitionInputPanel.add(new JLabel("Current State:"), gbc);
        gbc.gridx = 1;
        if (currentStateField == null) currentStateField = new JTextField(8);
        transitionInputPanel.add(currentStateField, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        transitionInputPanel.add(new JLabel("Next State:"), gbc);
        gbc.gridx = 3;
        if (nextStateField == null) nextStateField = new JTextField(8);
        transitionInputPanel.add(nextStateField, gbc);

        // Tape-specific fields
        readSymbolFields = new JTextField[currentNumTapes];
        writeSymbolFields = new JTextField[currentNumTapes];
        directionCombos = new JComboBox[currentNumTapes];

        for (int i = 0; i < currentNumTapes; i++) {
            gbc.gridx = 0; gbc.gridy = 1 + i;
            transitionInputPanel.add(new JLabel("Tape " + i + " Read:"), gbc);
            gbc.gridx = 1;
            readSymbolFields[i] = new JTextField(3);
            transitionInputPanel.add(readSymbolFields[i], gbc);

            gbc.gridx = 2;
            transitionInputPanel.add(new JLabel("Write:"), gbc);
            gbc.gridx = 3;
            writeSymbolFields[i] = new JTextField(3);
            transitionInputPanel.add(writeSymbolFields[i], gbc);

            gbc.gridx = 4;
            transitionInputPanel.add(new JLabel("Direction:"), gbc);
            gbc.gridx = 5;
            directionCombos[i] = new JComboBox<>(MultiTapeTransition.Direction.values());
            transitionInputPanel.add(directionCombos[i], gbc);
        }

        // Add transition button
        gbc.gridx = 0; gbc.gridy = 1 + currentNumTapes;
        gbc.gridwidth = 2;
        if (addTransitionButton == null) addTransitionButton = new JButton("Add Transition");
        transitionInputPanel.add(addTransitionButton, gbc);

        // Update transition table
        createTransitionTable();

        transitionInputPanel.revalidate();
        transitionInputPanel.repaint();
    }

    private void createTransitionTable() {
        // Create column names
        String[] columnNames = new String[3 + currentNumTapes * 3];
        columnNames[0] = "Current State";
        columnNames[1] = "Next State";
        int col = 2;
        for (int i = 0; i < currentNumTapes; i++) {
            columnNames[col++] = "T" + i + " Read";
            columnNames[col++] = "T" + i + " Write";
            columnNames[col++] = "T" + i + " Dir";
        }

        transitionTableModel = new DefaultTableModel(columnNames, 0);
        if (transitionTable == null) {
            transitionTable = new JTable(transitionTableModel);
            transitionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        } else {
            transitionTable.setModel(transitionTableModel);
        }
    }

    private JPanel createSimulationPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Create input panel
        createSimulationInputPanel();

        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        stepButton = new JButton("Step");
        runButton = new JButton("Run");
        pauseButton = new JButton("Pause");
        resetButton = new JButton("Reset");

        controlPanel.add(stepButton);
        controlPanel.add(runButton);
        controlPanel.add(pauseButton);
        controlPanel.add(resetButton);

        // Status panel
        JPanel statusPanel = new JPanel(new GridLayout(3, 1));
        stateLabel = new JLabel("State: No machine loaded");
        stepLabel = new JLabel("Steps: 0");
        statusLabel = new JLabel("Status: No machine");

        statusPanel.add(stateLabel);
        statusPanel.add(stepLabel);
        statusPanel.add(statusLabel);

        // Create tapes container
        createTapesContainer();

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(controlPanel, BorderLayout.CENTER);
        topPanel.add(statusPanel, BorderLayout.SOUTH);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(tapesScrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void createTapesContainer() {
        tapesContainer = new JPanel();
        tapesContainer.setLayout(new BoxLayout(tapesContainer, BoxLayout.Y_AXIS));

        tapePanels = new JPanel[currentNumTapes];
        for (int i = 0; i < currentNumTapes; i++) {
            tapePanels[i] = new JPanel();
            tapePanels[i].setPreferredSize(new Dimension(600, 80));
            tapePanels[i].setBorder(BorderFactory.createTitledBorder("Tape " + i));
            tapesContainer.add(tapePanels[i]);
        }

        tapesScrollPane = new JScrollPane(tapesContainer);
        tapesScrollPane.setPreferredSize(new Dimension(650, 400));
    }

    private void setupEventListeners() {
        // Configuration tab listeners
        addTransitionButton.addActionListener(e -> addTransition());
        removeTransitionButton.addActionListener(e -> removeSelectedTransition());
        clearTransitionsButton.addActionListener(e -> clearTransitions());
        createMachineButton.addActionListener(e -> createMachine());

        // Simulation tab listeners
        loadButton.addActionListener(e -> {
            if (machine != null) {
                // First reset the machine to ensure clean state
                machine.reset();

                // Load input to all available tapes
                int maxTapes = Math.min(inputFields.length, machine.getNumTapes());
                for (int i = 0; i < maxTapes; i++) {
                    String input = inputFields[i].getText().trim();
                    if (!input.isEmpty()) {
                        machine.loadInput(input, i);
                    } else {
                        // Load empty string for empty input fields
                        machine.loadInput("", i);
                    }
                }

                // Also handle case where machine has more tapes than input fields
                for (int i = inputFields.length; i < machine.getNumTapes(); i++) {
                    machine.loadInput("", i);
                }

                updateDisplay();
            }
        });

        stepButton.addActionListener(e -> {
            if (machine != null) {
                machine.start();
                machine.step();
                updateDisplay();
            }
        });

        runButton.addActionListener(e -> {
            if (machine != null) {
                machine.start();
                animationTimer.start();
            }
        });

        pauseButton.addActionListener(e -> {
            if (machine != null) {
                machine.pause();
                animationTimer.stop();
            }
        });

        resetButton.addActionListener(e -> {
            if (machine != null) {
                machine.reset();
                animationTimer.stop();
                updateDisplay();
            }
        });
    }

    private void addTransition() {
        String currentState = currentStateField.getText().trim();
        String nextState = nextStateField.getText().trim();

        if (currentState.isEmpty() || nextState.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill current and next state fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        char[] readSymbols = new char[currentNumTapes];
        char[] writeSymbols = new char[currentNumTapes];
        MultiTapeTransition.Direction[] directions = new MultiTapeTransition.Direction[currentNumTapes];

        for (int i = 0; i < currentNumTapes; i++) {
            String readStr = readSymbolFields[i].getText().trim();
            String writeStr = writeSymbolFields[i].getText().trim();

            if (readStr.isEmpty() || writeStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all tape fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (readStr.length() != 1 || writeStr.length() != 1) {
                JOptionPane.showMessageDialog(this, "Please fill all tape fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            readSymbols[i] = readStr.charAt(0);
            writeSymbols[i] = writeStr.charAt(0);
            directions[i] = (MultiTapeTransition.Direction) directionCombos[i].getSelectedItem();
        }

        // Add to transition table
        Object[] rowData = new Object[3 + currentNumTapes * 3];
        rowData[0] = currentState;
        rowData[1] = nextState;
        int col = 2;
        for (int i = 0; i < currentNumTapes; i++) {
            rowData[col++] = readSymbols[i];
            rowData[col++] = writeSymbols[i];
            rowData[col++] = directions[i];
        }
        transitionTableModel.addRow(rowData);

        // Clear input fields
        currentStateField.setText("");
        nextStateField.setText("");
        for (int i = 0; i < currentNumTapes; i++) {
            readSymbolFields[i].setText("");
            writeSymbolFields[i].setText("");
        }
    }

    private void removeSelectedTransition() {
        int selectedRow = transitionTable.getSelectedRow();
        if (selectedRow >= 0) {
            transitionTableModel.removeRow(selectedRow);
        }
    }

    private void clearTransitions() {
        transitionTableModel.setRowCount(0);
    }

    private void createMachine() {
        try {
            // Parse machine parameters
            Set<String> states = parseCommaSeparated(statesField.getText());
            Set<Character> alphabet = parseCharacterSet(alphabetField.getText());
            Set<Character> tapeAlphabet = parseCharacterSet(tapeAlphabetField.getText());
            String initialState = initialStateField.getText().trim();
            Set<String> acceptStates = parseCommaSeparated(acceptStatesField.getText());
            Set<String> rejectStates = parseCommaSeparated(rejectStatesField.getText());
            char blankSymbol = blankSymbolField.getText().charAt(0);
            int numTapes = (Integer) numTapesSpinner.getValue();

            // Create machine
            machine = new MultiTapeTuringMachineLogic(states, alphabet, tapeAlphabet,
                    initialState, acceptStates, rejectStates, blankSymbol, numTapes);

            // Add transitions from table (only if there are any)
            if (transitionTableModel.getRowCount() > 0) {
                for (int row = 0; row < transitionTableModel.getRowCount(); row++) {
                    String currentState = (String) transitionTableModel.getValueAt(row, 0);
                    String nextState = (String) transitionTableModel.getValueAt(row, 1);

                    char[] readSymbols = new char[numTapes];
                    char[] writeSymbols = new char[numTapes];
                    MultiTapeTransition.Direction[] directions = new MultiTapeTransition.Direction[numTapes];

                    int col = 2;
                    for (int i = 0; i < numTapes; i++) {
                        readSymbols[i] = transitionTableModel.getValueAt(row, col++).toString().charAt(0);
                        writeSymbols[i] = transitionTableModel.getValueAt(row, col++).toString().charAt(0);
                        directions[i] = (MultiTapeTransition.Direction) transitionTableModel.getValueAt(row, col++);
                    }

                    MultiTapeTransition transition = new MultiTapeTransition(
                            currentState, readSymbols, nextState, writeSymbols, directions);
                    machine.addTransition(transition);
                }
            }

            // Update simulation panel to match machine's number of tapes
            updateSimulationPanel();
            updateDisplay();

            // Only show success message if manually created (not auto-created by spinner)
            if (transitionTableModel.getRowCount() > 0) {
                JOptionPane.showMessageDialog(this, "Machine created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            // Silently fail for auto-creation, only show error for manual creation
            if (transitionTableModel.getRowCount() > 0) {
                JOptionPane.showMessageDialog(this, "Error creating machine: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateDisplay() {
        if (machine == null) {
            stateLabel.setText("State: No machine loaded");
            stepLabel.setText("Steps: 0");
            statusLabel.setText("Status: No machine");
            return;
        }

        stateLabel.setText("State: " + machine.getCurrentState());
        stepLabel.setText("Steps: " + machine.getStepCount());

        MachineState state = machine.getMachineState();
        statusLabel.setText("Status: " + state);

        // Update tape displays
        for (int i = 0; i < machine.getNumTapes() && i < tapePanels.length; i++) {
            updateTapeDisplay(i);
        }
    }

    private void updateTapeDisplay(int tapeIndex) {
        if (machine == null || tapeIndex >= tapePanels.length) return;

        Tape tape = machine.getTape(tapeIndex);
        tapePanels[tapeIndex].removeAll();
        tapePanels[tapeIndex].setLayout(new FlowLayout());

        // Display tape contents around current position
        int headPos = tape.getHeadPosition();
        int start = headPos - 10;
        int end = headPos + 10;

        // Get the tape content for the entire range at once
        String tapeContent = tape.getTapeContent(start, end);

        for (int i = start; i <= end; i++) {
            char symbol = tapeContent.charAt(i - start);

            JLabel cellLabel = new JLabel(String.valueOf(symbol));
            cellLabel.setOpaque(true);
            cellLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            cellLabel.setPreferredSize(new Dimension(25, 25));
            cellLabel.setHorizontalAlignment(SwingConstants.CENTER);

            if (i == headPos) {
                cellLabel.setBackground(Color.YELLOW);
            } else {
                cellLabel.setBackground(Color.WHITE);
            }

            tapePanels[tapeIndex].add(cellLabel);
        }

        tapePanels[tapeIndex].revalidate();
        tapePanels[tapeIndex].repaint();
    }

    private Set<String> parseCommaSeparated(String input) {
        Set<String> result = new HashSet<>();
        if (input != null && !input.trim().isEmpty()) {
            String[] parts = input.split(",");
            for (String part : parts) {
                String trimmed = part.trim();
                if (!trimmed.isEmpty()) {
                    result.add(trimmed);
                }
            }
        }
        return result;
    }

    private Set<Character> parseCharacterSet(String input) {
        Set<Character> result = new HashSet<>();
        if (input != null && !input.trim().isEmpty()) {
            String[] parts = input.split(",");
            for (String part : parts) {
                String trimmed = part.trim();
                if (trimmed.length() == 1) {
                    result.add(trimmed.charAt(0));
                }
            }
        }
        return result;
    }

    // Replace the updateSimulationPanel method with this fixed version:
    private void updateSimulationPanel() {
        if (machine == null) return;

        int machineNumTapes = machine.getNumTapes();

        // Update current number of tapes to match machine
        currentNumTapes = machineNumTapes;

        // Update spinner to match machine (temporarily remove listener to avoid recursion)
        var listeners = numTapesSpinner.getChangeListeners();
        for (var listener : listeners) {
            numTapesSpinner.removeChangeListener(listener);
        }
        numTapesSpinner.setValue(machineNumTapes);
        for (var listener : listeners) {
            numTapesSpinner.addChangeListener(listener);
        }

        // Rebuild input fields and tapes container to match machine's number of tapes
        rebuildSimulationInputPanel();
        rebuildTapesContainer();
    }

    // Also fix the rebuildSimulationInputPanel method to use currentNumTapes:
    private void rebuildSimulationInputPanel() {
        if (inputPanel == null) return;

        inputPanel.removeAll();

        // Use currentNumTapes instead of reading from spinner
        int newNumTapes = currentNumTapes;

        // Preserve existing input values
        String[] oldValues = new String[inputFields != null ? inputFields.length : 0];
        if (inputFields != null) {
            for (int i = 0; i < oldValues.length; i++) {
                oldValues[i] = inputFields[i].getText();
            }
        }

        // Create new input fields
        inputFields = new JTextField[newNumTapes];
        for (int i = 0; i < newNumTapes; i++) {
            JPanel tapeInputPanel = new JPanel(new FlowLayout());
            tapeInputPanel.add(new JLabel("Tape " + i + " Input:"));
            inputFields[i] = new JTextField(15);

            // Restore old value if available
            if (i < oldValues.length) {
                inputFields[i].setText(oldValues[i]);
            } else if (i == 0 && newNumTapes > 0) {
                inputFields[i].setText("101");
            }

            tapeInputPanel.add(inputFields[i]);
            inputPanel.add(tapeInputPanel);
        }

        JPanel loadPanel = new JPanel(new FlowLayout());
        loadPanel.add(loadButton);
        inputPanel.add(loadPanel);

        inputPanel.revalidate();
        inputPanel.repaint();

        // Also rebuild tapes container
        rebuildTapesContainer();
    }

    // Fix the rebuildTapesContainer method to use currentNumTapes:
    private void rebuildTapesContainer() {
        if (tapesContainer == null) return;

        tapesContainer.removeAll();

        // Use currentNumTapes instead of reading from spinner
        int newNumTapes = currentNumTapes;
        tapePanels = new JPanel[newNumTapes];

        for (int i = 0; i < newNumTapes; i++) {
            tapePanels[i] = new JPanel();
            tapePanels[i].setPreferredSize(new Dimension(600, 80));
            tapePanels[i].setBorder(BorderFactory.createTitledBorder("Tape " + i));
            tapesContainer.add(tapePanels[i]);
        }

        tapesContainer.revalidate();
        tapesContainer.repaint();
    }

    // Also update the createSimulationInputPanel method to be consistent:
    private void createSimulationInputPanel() {
        inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        // Initialize input fields for current number of tapes
        inputFields = new JTextField[currentNumTapes];
        for (int i = 0; i < currentNumTapes; i++) {
            JPanel tapeInputPanel = new JPanel(new FlowLayout());
            tapeInputPanel.add(new JLabel("Tape " + i + " Input:"));
            inputFields[i] = new JTextField(15);
            if (i == 0) inputFields[i].setText("101");
            tapeInputPanel.add(inputFields[i]);
            inputPanel.add(tapeInputPanel);
        }

        JPanel loadPanel = new JPanel(new FlowLayout());
        loadButton = new JButton("Load");
        loadPanel.add(loadButton);
        inputPanel.add(loadPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
            } catch (Exception e) {
                // Use default look and feel
            }
            new MultiTapeTuringMachineCreatorGUI().setVisible(true);
        });
    }
}