package de.my_domain.abalone.model;

/**
 * The Direction Enum.
 *
 * The Direction enum describes direction for the game {@link Abalone}.
 *
 * It provides six different directions in which the player is able to move. As
 * well as methods to obtain a element of direction and its offset, which
 * describes the move as an integer in row and diagonal direction.
 */
public enum Direction {

    /**
     * Represents the Direction Right.
     */
    RIGHT(0, 1),

    /**
     * Represent the Direction Upright.
     */
    UPRIGHT(1, 1),

    /**
     * Represents the Direction Upleft.
     */
    UPLEFT(1, 0),

    /**
     * Represents the Direction Left.
     */
    LEFT(0, -1),

    /**
     * Represents the Direction Downleft.
     */
    DOWNLEFT(-1, -1),

    /**
     * Represents the Direction Downright.
     */
    DOWNRIGHT(-1, 0);

    private int rowOffset;
    private int diagOffset;

    Direction(int rowOffset, int diagOffset) {
        this.rowOffset = rowOffset;
        this.diagOffset = diagOffset;
    }

    /**
     * The getRowOffset method is the getter Method of the variable rowOffset.
     *
     * The rowOffset is the shift in the row coordinate which must be done to
     * move in a certain direction.
     *
     * @return The rowOffset of an {@link Direction} enum will be returned.
     */
    public int getRowOffset() {
        return rowOffset;
    }

    /**
     * The getDiagOffset method is the getter Method of the variable diagOffset.
     *
     * The diagOffset is the shift in the diag coordinate which must be done to
     * move in a certain direction.
     *
     * @return The diagOffset of an {@link Direction} enum will be returned.
     */
    public int getDiagOffset() {
        return diagOffset;
    }

    /**
     * The getMove method takes initial coordinates and final coordinates of
     * a move and returns a direction representing this move.
     * Hereby it checks if the requested move corresponds to one of the six
     * legal directions. If this is not the case {@code null} will be returned.
     *
     * @param rowFrom Is the initial row coordinate.
     * @param diagFrom Is the initial diagonal coordinate.
     * @param rowTo Is the final row coordinate.
     * @param diagTo Is the final diag coordinate.
     * @return The direction of the requested move will be returned. If the
     *         requested move is illegal null will be returned.
     */
    public static Direction getMove(int rowFrom, int diagFrom, int rowTo,
                                     int diagTo) {
        Direction[] directions = Direction.values();
        int deltaRow = rowTo - rowFrom;
        int deltaDiag = diagTo - diagFrom;
        for (Direction dir : directions) {
            if (dir.rowOffset == deltaRow && dir.diagOffset == deltaDiag) {
                return dir;
            }
        }
        return null;
    }

    /**
     * The toString method overrides the toString method of the
     * {@link Object} class.
     *
     * @return The name and the corresponding offsets as a tuple will be
     * returned.
     */
    @Override
    public String toString() {
        return (name() + "(" + rowOffset + ", " + diagOffset + ")");
    }
}
