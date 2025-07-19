package Menu;

import finiteAutomata.FAFrame;
import finiteAutomata.FiniteAutomata;
import pushDownAutomata.PushDownAutomata;
import pushDownAutomataGraphics.PDATransitionGUI;
import turingMachine.MultiTapeTuringMachineCreatorGUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class TotalMenu extends JFrame {

    private static final Color BACKGROUND_COLOR = new Color(245, 245, 250);
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private static final Color HOVER_COLOR = new Color(100, 149, 237);
    private static final Color TEXT_COLOR = new Color(50, 50, 50);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 16);

    public TotalMenu() {
        super("Automata Theory Simulator");
        initializeMenu();
        createComponents();
    }

    private void initializeMenu() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 700);
        setMinimumSize(new Dimension(500, 600));
        setLocationRelativeTo(null);
        setBackground(BACKGROUND_COLOR);

        // Try to set system look and feel
        try {
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            // Continue with default look and feel
        }
    }

    private void createComponents() {
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(30, 30, 20, 30));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Automata Theory Simulator");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Select a computational model to explore");
        subtitleLabel.setFont(SUBTITLE_FONT);
        subtitleLabel.setForeground(TEXT_COLOR);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(subtitleLabel);

        return headerPanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 50, 20, 50));
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Finite Automata Button
        gbc.gridy = 0;
        JButton faButton = createStyledButton(
                "Finite Automata",
                "Design and simulate DFA and NFA",
                "🔄"
        );
        faButton.addActionListener(e -> new FAFrame(List.of(new FiniteAutomata<>(new ArrayList<>(), new ArrayList<>()))));
        mainPanel.add(faButton, gbc);

        // Push-down Automata Button
        gbc.gridy = 1;
        JButton pdaButton = createStyledButton(
                "Push-down Automata",
                "Create context-free language recognizers",
                "📚"
        );
        pdaButton.addActionListener(e -> new PDATransitionGUI<>(new PushDownAutomata<>()));
        mainPanel.add(pdaButton, gbc);

        // Turing Machine Button
        gbc.gridy = 2;
        JButton turingButton = createStyledButton(
                "Turing Machine",
                "Build universal computation models",
                "⚙️"
        );
        turingButton.addActionListener(ev -> SwingUtilities.invokeLater(() -> {
            try {
                new MultiTapeTuringMachineCreatorGUI().setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error launching Turing Machine: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }));
        mainPanel.add(turingButton, gbc);

        return mainPanel;
    }

    private JButton createStyledButton(String title, String description, String icon) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(400, 80));
        button.setBackground(Color.WHITE);
        button.setForeground(TEXT_COLOR);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(15, 20, 15, 20)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Icon panel
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLabel.setPreferredSize(new Dimension(40, 40));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Text panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(BUTTON_FONT);
        titleLabel.setForeground(TEXT_COLOR);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(new Color(120, 120, 120));

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(3));
        textPanel.add(descLabel);

        button.add(iconLabel, BorderLayout.WEST);
        button.add(textPanel, BorderLayout.CENTER);

        // Hover effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(248, 249, 250));
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(HOVER_COLOR, 2),
                        new EmptyBorder(14, 19, 14, 19)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                        new EmptyBorder(15, 20, 15, 20)
                ));
            }
        });

        return button;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(BACKGROUND_COLOR);
        footerPanel.setBorder(new EmptyBorder(20, 30, 30, 30));

        JLabel footerLabel = new JLabel("Select an option above to begin");
        footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footerLabel.setForeground(new Color(120, 120, 120));

        footerPanel.add(footerLabel);

        return footerPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TotalMenu());
    }
}