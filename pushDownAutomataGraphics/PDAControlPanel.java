package pushDownAutomataGraphics;

import pushDownAutomata.PushDownAutomata;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PDAControlPanel<K> extends JPanel {
    private final PushDownAutomata<K> pda;
    private JTextField inputField;
    private JTextField stackSymbolField;
    private JButton addInputButton;
    private JButton addStackSymbolButton;
    private JButton runSimulationButton;
    private JButton clearInputButton;
    private JButton clearStackButton;
    private JLabel inputQueueLabel;
    private JLabel statusLabel;

    public PDAControlPanel(PushDownAutomata<K> pda) {
        this.pda = pda;
        initializeComponents();
        setupLayout();
        addEventListeners();
    }

    private void initializeComponents() {
        // Input components
        inputField = new JTextField(10);
        inputField.setToolTipText("Enter symbol to add to input queue");

        addInputButton = new JButton("Add to Input");
        clearInputButton = new JButton("Clear Input");
        clearStackButton = new JButton("Clear Stack");

        // Stack components
        stackSymbolField = new JTextField(10);
        stackSymbolField.setToolTipText("Enter symbol to add to stack");

        addStackSymbolButton = new JButton("Add to Stack");

        // Simulation control
        runSimulationButton = new JButton("Run Simulation");
        runSimulationButton.setBackground(Color.GREEN);
        runSimulationButton.setForeground(Color.BLACK);
        runSimulationButton.setFont(new Font("Arial", Font.BOLD, 12));

        // Status components
        inputQueueLabel = new JLabel("Input Queue: []");
        statusLabel = new JLabel("Ready");
        statusLabel.setForeground(Color.BLUE);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("PDA Controls"));

        // Input panel
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input Queue"));
        inputPanel.add(new JLabel("Symbol:"));
        inputPanel.add(inputField);
        inputPanel.add(addInputButton);
        inputPanel.add(clearInputButton);

        // Stack panel
        JPanel stackPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        stackPanel.setBorder(BorderFactory.createTitledBorder("Stack"));
        stackPanel.add(new JLabel("Symbol:"));
        stackPanel.add(stackSymbolField);
        stackPanel.add(addStackSymbolButton);
        stackPanel.add(clearStackButton);

        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controlPanel.add(runSimulationButton);

        // Status panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(inputQueueLabel, BorderLayout.WEST);
        statusPanel.add(statusLabel, BorderLayout.EAST);

        // Main layout
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(inputPanel);
        topPanel.add(stackPanel);

        add(topPanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
    }

    private void addEventListeners() {
        addInputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addInputSymbol();
            }
        });

        clearInputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearInput();
            }
        });

        clearStackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearStack();
            }
        });

        addStackSymbolButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStackSymbol();
            }
        });

        runSimulationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runSimulation();
            }
        });

        // Allow Enter key to add input
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addInputSymbol();
            }
        });

        // Allow Enter key to add stack symbol
        stackSymbolField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStackSymbol();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void addInputSymbol() {
        String input = inputField.getText().trim();
        if (!input.isEmpty()) {
            try {
                // This is a simplified approach - you might need to adjust based on your K type
                K symbol = (K) input;
                inputField.setText("");
                statusLabel.setText("Symbol added to input queue");
                statusLabel.setForeground(Color.GREEN);
                pda.addInput(symbol);
            } catch (Exception ex) {
                statusLabel.setText("Error adding symbol: " + ex.getMessage());
                statusLabel.setForeground(Color.RED);
            }
        }
    }

    private void clearInput() {
        pda.clearInput();
        statusLabel.setText("Input queue cleared");
        statusLabel.setForeground(Color.BLUE);
    }

    private void clearStack() {
        pda.clearStack();
        statusLabel.setText("Stacks cleared");
        statusLabel.setForeground(Color.BLUE);
    }

    @SuppressWarnings("unchecked")
    private void addStackSymbol() {
        String input = stackSymbolField.getText().trim();
        if (!input.isEmpty()) {
            try {
                K symbol = (K) input;
                stackSymbolField.setText("");
                statusLabel.setText("Symbol added to stack");
                statusLabel.setForeground(Color.GREEN);
                pda.addStack(symbol);
            } catch (Exception ex) {
                statusLabel.setText("Error adding symbol: " + ex.getMessage());
                statusLabel.setForeground(Color.RED);
            }
        }
    }

    private void runSimulation() {
        try {
            statusLabel.setText("Running simulation...");
            statusLabel.setForeground(Color.ORANGE);
            runSimulationButton.setEnabled(false);

            // Run the simulation in a separate thread to avoid blocking the UI
            new Thread(() -> {
                    try {
                        boolean result = pda.evaluateString();

                        if (result) {
                            statusLabel.setText("String ACCEPTED!");
                            statusLabel.setForeground(Color.GREEN);
                        } else {
                            statusLabel.setText("String REJECTED!");
                            statusLabel.setForeground(Color.RED);
                        }
                    } catch (Exception ex) {
                        statusLabel.setText("Simulation error: " + ex.getMessage());
                        statusLabel.setForeground(Color.RED);
                    } finally {
                        runSimulationButton.setEnabled(true);
                    }
            }).start();

        } catch (Exception ex) {
            statusLabel.setText("Error starting simulation: " + ex.getMessage());
            statusLabel.setForeground(Color.RED);
            runSimulationButton.setEnabled(true);
        }
    }
}