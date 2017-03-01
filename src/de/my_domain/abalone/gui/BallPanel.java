package de.my_domain.abalone.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * The BallPanel Class.
 *
 * The BallPanel class extends {@link GridPanel} and is used to represent an
 * slot on the {@link BoardPanel}. It saves row and diag coordinates of the
 * corresponding slot as well as the color in which the slot is displayed. To
 * represent a edge panel of the game the color must simply be set to {@code
 * null}. A BallPanel can have distinct states which determine the look of
 * the BallPanel. It can be normal, selected or highlighted.
 *
 * The class provides methods to change the color and the state of each
 * BallPanel so that the BallPanel is mutable and that it can be reused.
 */
public class BallPanel extends GridPanel {

    /**
     * The NORMAL_STATE indicates that the BallPanel is normal and should be
     * displayed to the user using its normal, none selected nor highlighted
     * representation.
     */
    public static final int NORMAL_STATE = 1;

    /**
     * The SELECTED_STATE indicates that the BallPanel has been selected. It
     * then draws a circle in a different color, which is specified in
     * {@link DesignUtil} on top of the original color of the Ball. The user
     * then knows the ball he has selected.
     */
    public static final int SELECTED_STATE = 2;

    /**
     * The HIGHLIGHTED_STATE indicates that the BallPanel has been
     * highlighted, such that a move can be made in the direction in which
     * the highlighted ball lies. It then draws a circle in a different
     * color, which is specified in {@link DesignUtil} on top of the original
     * color of the Ball.
     */
    public static final int HIGHLIGHTED_STATE = 3;

    private int row;
    private int diag;
    private Color color;

    /**
     * The state of a Ball marks if a ball is normal, selected or highlighted.
     */
    private int state;

    /**
     * The BallPanel constructor creates a new BallPanel saving the row and
     * diagonal coordinates of the corresponding slot on the
     * {@link de.my_domain.abalone.model.Board} game.
     *
     * @param row The row coordinate of the corresponding slot.
     * @param diag The diag coordinate of the corresponding slot.
     */
    public BallPanel(int row, int diag) {
        super();
        this.row = row;
        this.diag = diag;
        this.color = null;
        this.state = NORMAL_STATE;
    }

    /**
     * The getRow method is a getter method of the row coordinate of the
     * corresponding slot of the BallPanel.
     *
     * @return The row coordinate of the corresponding slot of the BallPanel
     *         will be returned.
     */
    int getRow() {
        return row;
    }

    /**
     * The getDiag method is a getter method of the diag coordinate of the
     * corresponding slot of the BallPanel.
     *
     * @return The diag coordinate of the corresponding slot of the BallPanel
     *         will be returned.
     */
    int getDiag() {
        return diag;
    }

    /**
     * The getColor method is a getter method of the {@link Color} attribute
     * in which the Ball is painted.
     *
     * @return The color of the Ball is returned. {@code null} is returned if
     *         the BallPanel has no color.
     */
    Color getColor() {
        return color;
    }

    /**
     * The setColor method is a setter method of the {@link Color} attribute
     * in which the Ball is painted. The Ball will not be painted if
     * {@code color == null}. If and only if the color is changed by the
     * method, the ball will be repainted. This avoids unneeded repaints of
     * the BallPanels.
     *
     * @param color The new color that is assigned to the BallPanel.
     */
    void setColor(de.my_domain.abalone.model.Color color) {
        Color newColor = DesignUtil.abaloneToAwt(color);
        if (this.color != newColor) {
            this.color = newColor;
            repaint();
        }
    }

    /**
     * The setState method is a setter method of the state attribute which
     * decides if the ball is highlighted or selected. The Ball will not be
     * selected or highlighted if {@code state == NORMAL_STATE}. If and only
     * if the status is changed by the method, the ball will be repainted.
     * This avoids unneeded repaints of the BallPanels.
     *
     * @param state The new state that is assigned to the BallPanel.
     */
    void setState(int state) {
        if (this.state != state) {
            this.state = state;
            repaint();
        }
    }

    /**
     * The paintComponent method overrides the paintComponent method of
     * {@link GridPanel}. It calls the paintComponent method of the
     * {@link GridPanel} first to draw the horizontal line, then two diagonal
     * lines and the ball as a circle are drawn. Finally, it is evaluated if
     * the ball needs to be marked as selected or highlighted.
     *
     * The colors for the operations are set in {@link DesignUtil}.
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

        // draw X lines
        g2d.setColor(DesignUtil.LINE_COLOR);
        g2d.drawLine(0, 0, getSize().width, getSize().height);
        g2d.drawLine(0, getSize().height, getSize().width, 0);

        // calculate diameter
        int diameter = Math.min(getSize().height, getSize().width);

        // draw ball
        drawBall(g2d, diameter);

        // select or highlight ball
        drawStates(g2d, diameter);
    }

    /**
     * The drawBall method evaluates if the color of the ball is not
     * {@code null}. If this is the case then it draws a circle in the
     * {@code this} color and the given diameter. The circle is drawn
     * centered and perfectly symmetrical.
     *
     * @param g2d Is a {@link Graphics2D} object.
     * @param diameter Is the diameter of the drawn Ball as an integer.
     */
    private void drawBall(Graphics2D g2d, int diameter) {
        if (color != null) {
            g2d.setColor(color);
            int cornerX = (getSize().width - diameter) / 2;
            int cornerY = (getSize().height - diameter) / 2;
            g2d.fillOval(cornerX, cornerY, diameter, diameter);
        }
    }

    /**
     * The drawStates method evaluates which state the circle is in. If the
     * circle is selected or highlighted a smaller circle is drawn in the
     * center of the JPanel. The circle is drawn centered and perfectly
     * symmetrical. The colors of the circles are specified in
     * {@link DesignUtil}.
     *
     * @param g2d Is a {@link Graphics2D} object.
     * @param diameter Is the diameter of the drawn Ball as an integer.
     */
    private void drawStates(Graphics2D g2d, int diameter) {
        if (state == SELECTED_STATE) {
            g2d.setColor(DesignUtil.SELECTED_DOT);
            int markDiameter = (int) (0.5 * diameter);
            int markCornerX = (getSize().width - markDiameter) / 2;
            int markCornerY = (getSize().height - markDiameter) / 2;
            g2d.fillOval(markCornerX, markCornerY, markDiameter, markDiameter);
        } else if (state == HIGHLIGHTED_STATE) {
            g2d.setColor(DesignUtil.HIGHLIGHTED_DOT);
            int markDiameter = (int) (0.3 * diameter);
            int markCornerX = (getSize().width - markDiameter) / 2;
            int markCornerY = (getSize().height - markDiameter) / 2;
            g2d.fillOval(markCornerX, markCornerY, markDiameter, markDiameter);
        }
    }
}
