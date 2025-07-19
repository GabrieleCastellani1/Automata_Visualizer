package finiteAutomata;

import graphsGraphics.buttonLogic.ButtonConfiguration;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class FAButtonPanel extends JPanel {

    public FAButtonPanel(List<ButtonConfiguration> components) {
        // Set modern background color
        this.setBackground(new Color(248, 249, 250));

        // Add padding around the panel
        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Use GridBagLayout with improved spacing
        this.setLayout(new GridBagLayout());

        // Configure and add components
        components.forEach(c -> {
            c.component.setVisible(true);
            c.component.setEnabled(true);

            // Apply modern styling to buttons
            if (c.component instanceof JButton) {
                styleButton((JButton) c.component);
            }

            // Apply modern styling to text fields
            if (c.component instanceof JTextField) {
                styleTextField((JTextField) c.component);
            }

            // Add some spacing between components
            if (c.constraints != null) {
                c.constraints.insets = new Insets(5, 5, 5, 5);
            }

            this.add(c.component, c.constraints);
        });
    }

    private void styleButton(JButton button) {
        // Modern button styling
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        // Rounded corners effect (limited in Swing, but we can try)
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 86, 179), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));

        // Modern font
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 86, 179));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 123, 255));
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    private void styleTextField(JTextField textField) {
        // Modern text field styling
        textField.setBackground(Color.WHITE);
        textField.setForeground(new Color(33, 37, 41));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        // Modern font
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Set maximum dimensions to keep text fields smaller
        textField.setMaximumSize(new Dimension(200, 35));
        textField.setPreferredSize(new Dimension(150, 30));

        // Focus effects
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0, 123, 255), 2),
                        BorderFactory.createEmptyBorder(7, 11, 7, 11)
                ));
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
    }
}