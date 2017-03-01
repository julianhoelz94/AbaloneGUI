package de.my_domain.abalone.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 * The CountPanel Class.
 *
 * The CountPanel class offers an easy way to display an integer value inside
 * a {@link JPanel}. It also enables the caller owner of an object to set color
 * as well as the integer value that is displayed. It is taken care of that the
 * score will always fit perfectly in side the JPanel, no matter what size
 * the {@link JPanel} needs to have.
 */
public class CountPanel extends JPanel {

    private int score;
    private Color color;
    private BoardPanel parent;

    /**
     * The constructor of CountPanel overrides the standard constructor and
     * additionally sets the score to {@code 0} as well as the color to
     * {@code BLACK}, such that none of these values are ever undefined.
     * In addition, the CountPanel is set to have a transparent Background.
     *
     * @param parent Is the {@link BoardPanel} on which the count panel is
     *               located on.
     */
    public CountPanel(BoardPanel parent) {
        super();
        this.parent = parent;
        setOpaque(false);
    }

    /**
     * The setInformation method is used to update the Information displayed
     * on the CountPanel. The score as well as the color of the font can be
     * altered. After updating repaint will be called to graphically update the
     * representation of the panel only if the values have changed.
     *
     * @param score Is the new score which is displayed.
     * @param color Is the new color in which the score is displayed.
     */
    void setInformation(int score, de.my_domain.abalone.model.Color color) {
        Color newColor = DesignUtil.abaloneToAwt(color);
        if (this.score != score || this.color != newColor) {
            this.score = score;
            this.color =  newColor;
            repaint();
        }
    }

    /**
     * The paintComponent method overrides the paintComponent method of
     * {@link JPanel}. Here it is used to draw an integer value on the JPanel
     * as big as possible. Therefore first calculates the optimal font size
     * selects its font type from {@link DesignUtil} and then draws the String.
     * This method also recalculates the font size if the text first
     * calculated is to wide for the CountPanel. Therefore the text always
     * fits the CountPanel perfectly.
     *
     * @param g Is a {@link Graphics} object received from the caller of the
     *          method.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (color != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            Font font = parent.getFittingFont();
            String text = Integer.toString(score);
            int xCoord = (getWidth()
                    - getFontMetrics(font).stringWidth(text)) / 2;
            g2d.setColor(color);
            g2d.setFont(font);
            g2d.drawString(text, xCoord, getHeight() - 1);
        }
    }
}
