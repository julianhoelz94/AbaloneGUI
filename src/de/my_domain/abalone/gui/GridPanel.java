package de.my_domain.abalone.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 * The GridPanel Class.
 *
 * The GridPanel class is {@link JPanel} which sets the background to
 * transparent and paints a horizontal line across the middle of the panel.
 *
 * The GridPanel is mainly used to fill the gaps in between to slots on the
 * {@link BoardPanel}. It has no further functionality.
 */
public class GridPanel extends JPanel {

    /**
     * The constructor of GridPanel overrides the standard constructor.
     * In addition, the CountPanel is set to have a transparent Background.
     */
    public GridPanel() {
        super();
        setOpaque(false);
        setPreferredSize(new Dimension(DesignUtil.DEFAULT_BALL_SIZE));
    }

    /**
     * The paintComponent method overrides the paintComponent method of
     * {@link JPanel}. It is used to draw an horizontal line across the
     * GridPanel. The horizontal line is located in the middle of the height
     * of the GridPanel.
     *
     * @param g Is a {@link Graphics} object received from the caller of the
     *          method.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // draw horizontal lines
        g2d.setColor(DesignUtil.LINE_COLOR);
        g2d.drawLine(0, getSize().height / 2,
                getSize().width, getSize().height / 2);
    }
}
