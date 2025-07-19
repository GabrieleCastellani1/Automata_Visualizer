package pushDownAutomataGraphics;

import pushDownAutomata.PushDownAutomata;
import util.Util;

import javax.swing.*;
import java.awt.*;

public class PDAFrame<K> extends JFrame{

    public <K> PDAFrame(PushDownAutomata<K> pda){
        super();
        this.setPreferredSize(new Dimension(Util.FRAMEWIDTH, Util.FRAMEHEIGHT));
        this.setResizable(true);
        this.setVisible(true);
        PDAPanel<?> panel = new PDAPanel<>(pda);

        PDAControlPanel<K> PDAControlPanel = new PDAControlPanel<>(pda);
        Dimension preferredDimension = new Dimension(Util.FRAMEWIDTH, Util.FRAMEHEIGHT);
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(preferredDimension);

        JScrollPane scrollPane = new JScrollPane(
                panel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        JViewport viewport = new JViewport(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                panel.paintComponent(g);
                repaint();
            }
        };

        viewport.setView(panel);
        scrollPane.setViewport(viewport);

        JSplitPane container = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        container.setTopComponent(scrollPane);
        container.setBottomComponent(PDAControlPanel);
        container.setDividerLocation(Util.FRAMEWIDTH/2);
        container.setPreferredSize(preferredDimension);
        container.setVisible(true);

        this.add(container);
        this.pack();
    }
}
