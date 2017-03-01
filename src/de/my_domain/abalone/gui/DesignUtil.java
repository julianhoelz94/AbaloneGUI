package de.my_domain.abalone.gui;

import java.awt.Color;
import java.awt.Dimension;

/**
 * The final DesignUtil Class.
 *
 * The DesignUtil final class is a utility class which offers constants and
 * default values and colors, as well as a convert method for
 * {@link de.my_domain.abalone.model.Color} objects to be converted into
 * {@link Color} objects, for the AbaloneGUI.
 */
public final class DesignUtil {

    /**
     * The DesignUtil constructor is explicitly set to private and throws an
     * {@link UnsupportedOperationException} so that it is never called.
     *
     * @throws UnsupportedOperationException when called.
     */
    private DesignUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * Represents the title of the {@link GameFrame} window.
     */
    public static final String TITLE = "Abalone";

    /**
     * Represents the background color of the {@link BoardPanel}.
     */
    public static final Color BACKGROUND = new Color(255, 211, 155);

    /**
     * Represents the line color of the grid behind the Balls on the
     * {@link BallPanel}.
     */
    public static final Color LINE_COLOR = new Color(150, 150, 150, 150);

    /**
     * Represents the color the smaller dot has if a {@link BallPanel} is
     * selected.
     */
    public static final Color SELECTED_DOT = Color.BLUE;

    /**
     * Represents the color the smaller dot has if a {@link BallPanel} is
     * highlighted.
     */
    public static final Color HIGHLIGHTED_DOT = Color.GREEN;

    /**
     * Represents the font type the in which the {@link CountPanel} prints
     * its numbers.
     */
    public static final String FONT = "SansSerif";

    /**
     * Represents an approximate default ball size, from which the minimum
     * {@link GameFrame} size is calculated.
     */
    public static final Dimension DEFAULT_BALL_SIZE = new Dimension(30, 30);

    /**
     * Represents the AWT color for a black {@link BallPanel} slot.
     */
    public static final Color BLACK_SLOT = Color.BLACK;

    /**
     * Represents the AWT color for a white {@link BallPanel} slot.
     */
    public static final Color WHITE_SLOT = Color.WHITE;

    /**
     * Represents the AWT color for an empty {@link BallPanel} slot.
     */
    public static final Color EMPTY_SLOT = Color.LIGHT_GRAY;

    /**
     * The abaloneToAwt method is a static method which transforms the three
     * possible {@link de.my_domain.abalone.model.Color} Objects, which are
     * used in the {@link de.my_domain.abalone.model.Abalone} Game, into
     * {@link Color}, which are used in AWT to represent the color of
     * BallPanels.
     *
     * @param color is an object of the type
     *              {@link de.my_domain.abalone.model.Color}
     * @return The colors
     *         {@code BLACK_SLOT} if color is {@code BLACK},
     *         {@code WHITE_SLOT} if color is {@code WHITE},
     *         {@code EMPTY_SLOT} if color is {@code EMPTY},
     *         are returned.
     */
    public static Color abaloneToAwt(de.my_domain.abalone.model.Color color) {
        switch (color) {
        case BLACK:
            return BLACK_SLOT;
        case WHITE:
            return WHITE_SLOT;
        case EMPTY:
            return EMPTY_SLOT;
        default:
            throw new IllegalArgumentException("No Color!");
        }
    }
}
