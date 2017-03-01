package de.my_domain.abalone.model;

/**
 * The Ball Wrapper Class.
 *
 * Ball is a Wrapper Class for the enum types Color and Player. It also saves
 * a coordinate so that a ball can be clearly assigned to a slot on the board
 * of a Game.
 *
 * The class provides methods to make it possible to get its color, as its
 * owner and its current position. It als provides setter to change the
 * latter and thus move the object.
 */
public class Ball implements Cloneable {

    private Color color;
    private Player owner;
    private int row;
    private int diag;

    /**
     * The Ball constructor returns a new {@link Ball} object and sets its
     * parameters as given by the following parameters.
     *
     * @param color Is the color of the ball, represented by an object of the
     *              enum {@link Color}.
     * @param owner Is the owner or rather player which owns the returned
     *              ball, given by an object of the enum {@link Player}.
     * @param row Is the current row coordinate of the object.
     * @param diag Is the current diagonal coordinate of the object.
     */
    public Ball(Color color, Player owner, int row, int diag) {
        this.color = color;
        this.owner = owner;
        this.row = row;
        this.diag = diag;
    }

    /**
     * The getColor method is the getter method of the attribute Color.
     *
     * @return The color of {@code this} will be returned as a enum
     * {@link Color}
.    */
    public Color getColor() {
        return color;
    }

    /**
     * The getRow method is the getter method of the row coordinate of a ball.
     *
     * @return The row coordinate will be returned as a primitive integer.
     */
    public int getRow() {
        return row;
    }

    /**
     * The getDiag method is the getter method of the diagonal coordinate of a
     * ball.
     *
     * @return The diagonal coordinate will be returned as a primitive integer.
     */
    public int getDiag() {
        return diag;
    }

    /**
     * The moveCoordinates method is a special setter method for both, the
     * row and the diagonal coordinates. It only takes a direction in which
     * {@code this} object should be moved and changes its coordinates
     * respectively.
     *
     * @param dir Is the direction in which the ball needs to move as an enum
     *            {@link Direction}.
     */
    public void moveCoordinates(Direction dir) {
        this.row = Utility.getNextRowCoord(row, dir);
        this.diag = Utility.getNextDiagCoord(diag, dir);
    }

    /**
     * The isHuman method is a query method, which is called when one wants
     * to know if a ball has an owner and whether the ball's owner is human
     * or not.
     *
     * @return {@code true} if the ball has an owner an it is
     *                      {@link Player}{@code .HUMAN}.
     *         {@code false} if the ball does not have an owner or if the
     *                       owner is {@link Player}{@code .MACHINE}.
     */
    public boolean isHuman() {
        return (owner != null) && owner.isHuman();
    }

    /**
     * The isMachine method is a query method, which is called when one wants
     * to know if a ball has an owner and whether the ball's owner is machine
     * or not.
     *
     * @return {@code true} if the ball has an owner an it is
     *                      {@link Player}{@code .MACHINE}.
     *         {@code false} if the ball does not have an owner or if the
     *                       owner is {@link Player}{@code .HUMAN}.
     */
    public boolean isMachine() {
        return (owner != null) && owner.isMachine();
    }

    /**
     * The isEmpty method is a query method, which is called when one wants
     * to know if a ball has a color or solely is a placeholder for an empty
     * slot.
     *
     * @return {@code true} if the ball is a placeholder and thus is color is
     *                      {@link Color}{@code .EMPTY}
     *         {@code false} if the ball is not a placeholder and thus has a
     *                       color and belongs to an owner.
     */
    public boolean isEmpty() {
        return this.color == Color.EMPTY;
    }

    /**
     * The clone method overrides the clone method of the
     * class {@link Object}. It creates a deep copy of {@code this} and makes
     * the access to this method {@code public}.
     *
     * @return A deep copy of {@code this} will be returned.
     * @throws CloneNotSupportedException Is thrown when clone() is called on
     *                                    an object that does not implement
     *                                    the mark-up interface
     *                                    {@link Cloneable}.
     */
    @Override
    public Ball clone() throws CloneNotSupportedException {
        return (Ball) super.clone();
    }

    /**
     * The toString method overrides the toString method of the
     * class {@link Object}. It returns a string representation of {@code this}
     * using the string representation of the enum {@link Color}.
     *
     * @return {@code "X"} if the color of {@code this} is {@code BLACK}
     *         {@code "0"} if the color of {@code this} is {@code WHITE}
     *         {@code "."} if the color of {@code this} is {@code EMPTY}
     */
    @Override
    public String toString() {
        return color.toString();
    }
}
