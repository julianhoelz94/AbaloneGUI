package de.my_domain.abalone.model;

/**
 * The Color Enum.
 *
 * The Color enum describes the color of a {@link Player} in a game which
 * implements the interface {@link Board}.
 */
public enum Color {

    /**
     * Represents the color black.
     */
    BLACK("X"),

    /**
     * Represents the color white.
     */
    WHITE("O"),

    /**
     * Represents the fact that no color has been assigned, say an empty field.
     */
    EMPTY(".");

    private String representation;

    Color(String representation) {
        this.representation = representation;
    }

    /**
     * The getOpponent method returns the opponent color of {@code this}.
     *
     * @return {@code BLACK} if {@code this} is {@code WHITE}
     *         {@code WHITE} if {@code this} is {@code BLACK}
     * @throws IllegalArgumentException if the method is called on the color
     *                                  {@code EMPTY}.
     */
    public Color getOpponentColor() {
        switch (this) {
            case BLACK:
                return Color.WHITE;
            case WHITE:
                return Color.BLACK;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * The toString method overrides the toString method of the
     * {@link Object} class.
     *
     * @return {@code "X"} if the color is {@code BLACK}
     *         {@code "0"} if the color is {@code WHITE}
     *         {@code "."} if the color is {@code EMPTY}
     */
    @Override
    public String toString() {
        return representation;
    }
}
