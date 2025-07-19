package finiteAutomata;

import finiteAutomata.buttons.factories.IOInitializerButtonFactory;
import graphs.AbstractGraph;
import graphsGraphics.AbstractGraphPanel;
import graphsGraphics.GraphButtonPanel;
import graphsGraphics.buttonLogic.ButtonInitializer;
import util.Util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class FAFrame extends JFrame {

    public <K, L> FAFrame(List<AbstractGraph<K, L>> graphs) {
        super("Finite Automata Visualizer");

        // Modern frame setup
        setupFrame();

        // Create main components with modern styling
        AbstractGraphPanel<K, L> panel = new AbstractGraphPanel<>(graphs);
        ButtonInitializer<K,L> buttonInitializer = new IOInitializerButtonFactory().createInitializer(graphs);
        GraphButtonPanel buttonPanel = new GraphButtonPanel(buttonInitializer.getAllComponents());

        // Style the graph panel
        styleGraphPanel(panel);

        // Style the button panel
        styleButtonPanel(buttonPanel);

        // Create modern scroll pane
        JScrollPane scrollPane = createModernScrollPane(panel);

        // Create modern split pane
        JSplitPane container = createModernSplitPane(scrollPane, buttonPanel);

        // Add to frame and finalize
        this.add(container);
        this.pack();

        // Center the frame on screen
        this.setLocationRelativeTo(null);
    }

    private void setupFrame() {
        Dimension preferredDimension = new Dimension(Util.FRAMEWIDTH, Util.FRAMEHEIGHT);

        this.setPreferredSize(preferredDimension);
        this.setResizable(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Modern frame styling
        this.setBackground(Color.WHITE);
        this.getContentPane().setBackground(Color.WHITE);

        // Set application icon (you can replace with your own icon)
        try {
            // This is a placeholder - replace with your actual icon
            this.setIconImage(createDefaultIcon());
        } catch (Exception e) {
            // Icon loading failed, continue without icon
        }
    }

    private Image createDefaultIcon() {
        // Create a simple default icon
        int size = 32;
        java.awt.image.BufferedImage icon = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = icon.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw a simple automata-like icon
        g2d.setColor(new Color(0, 123, 255));
        g2d.fillOval(4, 4, 24, 24);
        g2d.setColor(Color.WHITE);
        g2d.fillOval(8, 8, 16, 16);
        g2d.setColor(new Color(0, 123, 255));
        g2d.fillOval(12, 12, 8, 8);

        g2d.dispose();
        return icon;
    }

    private <K, L> void styleGraphPanel(AbstractGraphPanel<K, L> panel) {
        Dimension preferredDimension = new Dimension(Util.FRAMEWIDTH, Util.FRAMEHEIGHT);
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(preferredDimension);
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    private void styleButtonPanel(GraphButtonPanel buttonPanel) {
        buttonPanel.setBackground(new Color(248, 249, 250));
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(221, 223, 226)),
                new EmptyBorder(15, 15, 15, 15)
        ));
    }

    private <K, L> JScrollPane createModernScrollPane(AbstractGraphPanel<K, L> panel) {
        JScrollPane scrollPane = new JScrollPane(
                panel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        // Modern scroll pane styling
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Custom viewport with improved painting
        JViewport viewport = new JViewport() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                panel.paintComponent(g2d);
                g2d.dispose();
                repaint();
            }
        };

        viewport.setView(panel);
        viewport.setBackground(Color.WHITE);
        scrollPane.setViewport(viewport);

        // Style scrollbars
        styleScrollBars(scrollPane);

        return scrollPane;
    }

    private void styleScrollBars(JScrollPane scrollPane) {
        // Attempt to style scrollbars (limited in standard Swing)
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(206, 212, 218);
                this.thumbDarkShadowColor = new Color(173, 181, 189);
                this.thumbHighlightColor = new Color(233, 236, 239);
                this.thumbLightShadowColor = new Color(206, 212, 218);
                this.trackColor = new Color(248, 249, 250);
                this.trackHighlightColor = new Color(233, 236, 239);
            }
        });

        scrollPane.getHorizontalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(206, 212, 218);
                this.thumbDarkShadowColor = new Color(173, 181, 189);
                this.thumbHighlightColor = new Color(233, 236, 239);
                this.thumbLightShadowColor = new Color(206, 212, 218);
                this.trackColor = new Color(248, 249, 250);
                this.trackHighlightColor = new Color(233, 236, 239);
            }
        });
    }

    private JSplitPane createModernSplitPane(JScrollPane scrollPane, GraphButtonPanel buttonPanel) {
        JSplitPane container = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        container.setRightComponent(scrollPane);
        container.setLeftComponent(buttonPanel);

        // Modern split pane styling - FIXED: Allow full range movement
        container.setDividerSize(8);
        container.setContinuousLayout(true);
        container.setOneTouchExpandable(false);

        // REMOVED: Fixed resize weight that was constraining movement
        // container.setResizeWeight(0.65);

        // Set minimum sizes to allow shrinking to almost nothing
        scrollPane.setMinimumSize(new Dimension(0, 20)); // Allow top to shrink to almost nothing
        buttonPanel.setMinimumSize(new Dimension(0, 20)); // Allow bottom to shrink to almost nothing

        // Set initial divider location without constraining future movement
        SwingUtilities.invokeLater(() -> {
            int initialLocation = (int) (container.getWidth() * 0.35);
            if (initialLocation > 0) {
                container.setDividerLocation(initialLocation);
            }
        });

        container.setPreferredSize(new Dimension(Util.FRAMEWIDTH, Util.FRAMEHEIGHT));
        container.setBorder(BorderFactory.createEmptyBorder());
        container.setBackground(Color.WHITE);

        // Create a custom UI for better divider styling
        container.setUI(new javax.swing.plaf.basic.BasicSplitPaneUI() {
            @Override
            public javax.swing.plaf.basic.BasicSplitPaneDivider createDefaultDivider() {
                return new javax.swing.plaf.basic.BasicSplitPaneDivider(this) {
                    @Override
                    public void paint(Graphics g) {
                        Graphics2D g2d = (Graphics2D) g.create();
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        // Background
                        g2d.setColor(new Color(221, 223, 226));
                        g2d.fillRect(0, 0, getSize().width, getSize().height);

                        // Add a subtle grip indicator
                        g2d.setColor(new Color(173, 181, 189));
                        int centerY = getSize().height / 2;
                        int centerX = getSize().width / 2;
                        for (int i = -9; i <= 9; i += 3) {
                            g2d.fillOval(centerX - 1, centerY + i - 1, 2, 2);
                        }

                        g2d.dispose();
                    }

                    {
                        setFocusable(true);

                        addMouseListener(new java.awt.event.MouseAdapter() {
                            @Override
                            public void mouseEntered(java.awt.event.MouseEvent evt) {
                                setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
                                repaint();
                            }

                            @Override
                            public void mouseExited(java.awt.event.MouseEvent evt) {
                                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                                repaint();
                            }
                        });
                    }
                };
            }
        });

        return container;
    }
}