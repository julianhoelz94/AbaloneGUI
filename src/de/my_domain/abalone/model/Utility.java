package de.my_domain.abalone.model;

import java.util.List;

/**
 * The final Utility Class.
 *
 * The Utility class contains static helper methods for the Abalone Game. It
 * provides methods to calculate next coordinates and the extrema of lists of
 * the type {@link Node}.
 */
public final class Utility {

    private Utility() {
        throw new UnsupportedOperationException();
    }

    /**
     * The getNextRowCoord method calculates the next row coordinate given an
     * initial row coordinate and a direction in which the coordinate should
     * be moved.
     *
     * @param row Is the initial {@code row} coordinate which should be shifted.
     * @param dir Is the {@link Direction} in which the row coordinate is
     *            shifted.
     * @return The new row coordinate after moving it in a certain direction
     *         will be returned.
     */
    public static int getNextRowCoord(int row, Direction dir) {
        if (dir == null) {
            throw new IllegalArgumentException();
        }
        return row + dir.getRowOffset();
    }

    /**
     * The getNextDiagCoord method calculates the next diagonal coordinate
     * given an initial diagonal coordinate and a direction in which the
     * coordinate should be moved.
     *
     * @param diag Is the initial {@code diag} coordinate which should be
     *             shifted.
     * @param dir Is the {@link Direction} in which the diag coordinate is
     *            shifted.
     * @return The new diagonal coordinate after moving it in a certain
     *         direction will be returned.
     */
    public static int getNextDiagCoord(int diag, Direction dir) {
        if (dir == null) {
            throw new IllegalArgumentException();
        }
        return diag + dir.getDiagOffset();
    }

    /**
     * The max method return traverses the list {@code list} and
     * returns the first maximal object it can find.
     *
     * @param list Is the list it traverses to find the maximum.
     * @return Of all the maximums in the list, the first which could be
     *         found will be returned.
     * @throws IllegalArgumentException If {@code list} is null or empty.
     */
    public static Node max(List<Node> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException();
        }
        Node max = list.get(0);
        for (Node element : list) {
            if (max.compareTo(element) < 0) {
                max = element;
            }
        }
        return max;
    }

    /**
     * The min method return traverses the list {@code list} and
     * returns the first minimal object it can find.
     *
     * @param list Is the list it traverses to find the minimum.
     * @return Of all the minimums in the list, the first which could be
     *         found will be returned.
     * @throws IllegalArgumentException If {@code list} is null or empty.
     */
    public static Node min(List<Node> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException();
        }
        Node min = list.get(0);
        for (Node element : list) {
            if (min.compareTo(element) > 0) {
                min = element;
            }
        }
        return min;
    }
}
