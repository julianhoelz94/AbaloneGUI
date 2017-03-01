package de.my_domain.abalone.model;

import java.util.Collections;
import java.util.LinkedList;

/**
 * The Abalone Game implementation.
 *
 * Abalone is a class which implements the family game Abalone using the
 * Interface {@link Board}. This class allows a game play against a machine.
 *
 * The class implements the interface {@link Board} as well as the mark-up
 * interface {@link Cloneable}. It provides all the methods needed to play
 * the game via methods declared in the interface Board.
 */
public class Abalone implements Board, Cloneable {

    private static final int SMALLEST_LEVEL = 1;
    private static final int THIRD_LINE_OFFSET = 2;
    private static final double DETERMINE_WIN_CONSTANT = 5000000.0;
    private static final Color FIRST_COLOR = Color.BLACK;

    /**
     * Is used as a distinct static placeholder for an empty field.
     */
    private static final Ball EMPTY_BALL = new Ball(Color.EMPTY, null, -1, -1);

    private int size;
    private int level;
    private Player first;
    private Player next;
    private Ball[][] board;
    private LinkedList<Ball> humanBalls;
    private LinkedList<Ball> machineBalls;

    /**
     * The Abalone constructor returns {@link Abalone} object and sets its
     * size, difficulty of the machine player as level, and the first player
     * which makes a move.
     *
     * It also initializes the board and the two ball lists which inherit the
     * machine and the human balls.
     *
     * @param size Is the size of the returned board.
     * @param level Is the difficulty of the machine player.
     * @param first Is the first player allowed to make a move.
     * @throws IllegalArgumentException When the size is smaller than 7 or
     *                                  odd and when the level is set to a
     *                                  number smaller than 1 an Exception is
     *                                  thrown.
     */
    public Abalone(int size, int level, Player first) {
        if (level < SMALLEST_LEVEL || size < Board.MIN_SIZE || size % 2 != 1) {
            throw new IllegalArgumentException();
        } else {
            this.size = size;
            this.level = level;
        }

        this.first = first;
        this.next = first;
        this.board = new Ball[size][size];
        this.humanBalls = new LinkedList<>();
        this.machineBalls = new LinkedList<>();
        prepareBoard();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getOpeningPlayer() {
        return first;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Color getHumanColor() {
        return (first == Player.HUMAN ? FIRST_COLOR
                : FIRST_COLOR.getOpponentColor());
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException If the game is already over.
     */
    @Override
    public Player getNextPlayer() {
        return next;
    }

    /**
     * Checks if the provided coordinates are valid slots within the game.
     *
     * @param row Is the row coordinate of the slot.
     * @param diag Is the diagonal coordinate of the slot.
     * @return {@code true} If the coordinates represent a valid position.
     */
    @Override
    public boolean isValidPosition(int row, int diag) {
        return (row >= 0 && row < size && diag <= lastSlot(row)
                && diag >= firstSlot(row));
    }

    /**
     * Checks if the provided coordinates are valid slots within the game or 1
     * slot outside.
     *
     * @param row Is the row coordinate of the slot.
     * @param diag Is the diagonal coordinate of the slot.
     * @return {@code true} If the coordinates represent a valid target.
     */
    @Override
    public boolean isValidTarget(int row, int diag) {
        return (row >= -1 && row <= size && diag <= lastSlot(row) + 1
                && diag >= firstSlot(row) - 1);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException If the game is already over, or it is not
     *         the human's turn.
     * @throws IllegalArgumentException If the provided parameters are invalid,
     *         e.g., the from slot lies outside the grid or the to slot outside
     *         the grid plus an one-element border.
     */
    @Override
    public Board move(int rowFrom, int diagFrom, int rowTo, int diagTo) {
        Direction dir;
        Ball toMove;
        if (next.isMachine() || isGameOver()) {
            throw new IllegalStateException();
        } else if (!isValidPosition(rowFrom, diagFrom)
                || !isValidTarget(rowTo, diagTo)) {
            throw new IllegalArgumentException();
        } else {
            dir = Direction.getMove(rowFrom, diagFrom, rowTo, diagTo);
            toMove = board[rowFrom][diagFrom];
        }

        Abalone nextState = null;
        if (dir != null && toMove.isHuman()) {
            nextState = executeMove(toMove, dir);
        }
        return nextState;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException If the game is already over, or it is not
     *         the machine's turn.
     */
    @Override
    public Board machineMove() {
        if (next.isHuman() || isGameOver()) {
            throw new IllegalStateException();
        } else {
            return getBestMove();
        }
    }

    /**
     * Sets the skill level of the machine. The level is also the depth of
     * search for the recursive tree which determines the best move to be
     * made by the machine.
     *
     * @param level The skill as a number, must be at least 1.
     */
    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGameOver() {
        return (initBallCount() - humanBalls.size() >= Board.ELIM
                || initBallCount() - machineBalls.size() >= Board.ELIM);
    }

    /**
     * {@inheritDoc}
     *
     * @return The winner as an enum {@link Player}.
     * @throws IllegalStateException If the game is not over yet, then there is
     *                               no winner.
     */
    @Override
    public Player getWinner() {
        if (!isGameOver()) {
            throw new IllegalStateException("Game not over.");
        } else {
            return (humanBalls.size() < machineBalls.size() ? Player.MACHINE
                    : Player.HUMAN);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException If the the color is {@code null} or
     *                                  {@code EMPTY}
     */
    @Override
    public int getNumberOfBalls(Color color) {
        if (color == null || color == Color.EMPTY) {
            throw new IllegalArgumentException();
        } else {
            return (getHumanColor() == color ? humanBalls.size()
                    : machineBalls.size());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException If the given coordinates do not
     *                                  represent a slot on the board.
     */
    @Override
    public Color getSlot(int row, int diag) {
        if (!isValidPosition(row, diag)) {
            throw new IllegalArgumentException();
        } else {
            return board[row][diag].getColor();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder repr = new StringBuilder();
        for (int row = size - 1; 0 <= row; --row) {
            for (int index = 0; index < emptySpaces(row); ++index) {
                repr.append(" ");
            }
            for (int diag = firstSlot(row); diag <= lastSlot(row); ++diag) {
                Ball content = board[row][diag];
                repr.append(content.toString());
                if (!(lastSlot(row) == diag)) {
                    repr.append(" ");
                }
            }
            if (!(row == 0)) {
                repr.append("\n");
            }
        }
        return repr.toString();
    }

    /**
     * The clone method overrides the clone method of the
     * class {@link Object}. It creates a deep copy of {@code this}.
     *
     * @return A deep copy of {@code this} will be returned.
     * @throws CloneNotSupportedException Is thrown when clone() is called on
     *                                    an object that does not implement
     *                                    the mark-up interface
     *                                    {@link Cloneable}.
     */
    @Override
    protected Abalone clone() throws CloneNotSupportedException {
        Abalone copy = (Abalone) super.clone();
        Ball[][] boardCopy = board.clone();
        for (int row = 0; row < size; ++row) {
            boardCopy[row] = board[row].clone();
        }
        LinkedList<Ball> copyHumanBalls = new LinkedList<>();
        LinkedList<Ball> copyMachineBalls = new LinkedList<>();
        for (Ball element : humanBalls) {
            Ball slotCopy = element.clone();
            copyHumanBalls.add(slotCopy);
            boardCopy[slotCopy.getRow()][slotCopy.getDiag()] = slotCopy;
        }
        for (Ball element : machineBalls) {
            Ball slotCopy = element.clone();
            copyMachineBalls.add(slotCopy);
            boardCopy[slotCopy.getRow()][slotCopy.getDiag()] = slotCopy;
        }
        copy.humanBalls = copyHumanBalls;
        copy.machineBalls = copyMachineBalls;
        copy.board = boardCopy;
        return copy;
    }

    private int initBallCount() {
        return 2 + 3 * (size - 1) / 2;
    }

    private int emptySpaces(int row) {
        int middle = Math.floorDiv(size, 2);
        return Math.abs(row - middle);
    }

    private int firstSlot(int row) {
        return Math.max(0, row - Math.floorDiv(size, 2));
    }

    private int lastSlot(int row) {
        return Math.min(size - 1, row + Math.floorDiv(size, 2));
    }

    private void prepareBoard() {
        Color human = getHumanColor();
        Color machine = human.getOpponentColor();
        for (int row = 0; row < size; ++row) {
            for (int diag = firstSlot(row); diag <= lastSlot(row); ++diag) {
                if (row <= 1) {
                    insertBall(new Ball(human, Player.HUMAN,
                            row, diag));
                } else if (row == 2) {
                    if (diag < firstSlot(row) + THIRD_LINE_OFFSET
                            || diag > lastSlot(row) - THIRD_LINE_OFFSET) {
                        fillWithEmpty(row, diag);
                    } else {
                        insertBall(new Ball(human, Player.HUMAN,
                                row, diag));
                    }
                } else if (row == size - 3) {
                    if (diag < firstSlot(row) + THIRD_LINE_OFFSET
                            || diag > lastSlot(row) - THIRD_LINE_OFFSET) {
                        fillWithEmpty(row, diag);
                    } else {
                        insertBall(new Ball(machine, Player.
                                MACHINE, row, diag));
                    }
                } else if (row >= size - 2) {
                    insertBall(new Ball(machine, Player.MACHINE, row,
                            diag));
                } else {
                    fillWithEmpty(row, diag);
                }
            }
        }
        Collections.reverse(machineBalls);
    }

    private Ball fillWithEmpty(int row, int diag) {
        Ball slot = board[row][diag];
        board[row][diag] = EMPTY_BALL;
        return slot;
    }

    private void insertArray(Ball cell) {
        board[cell.getRow()][cell.getDiag()] = cell;
    }

    private void insertBall(Ball cell) {
        board[cell.getRow()][cell.getDiag()] = cell;
        if (cell.isHuman()) {
            humanBalls.add(cell);
        } else if (cell.isMachine()) {
            machineBalls.add(cell);
        }
    }

    /**
     * The executeMove method is the method which returns the new, cloned
     * board, on which the moveBall method has already conducted the move. It
     * also sets the next player of returned board by testing if a next move
     * for the opponent player is possible.
     *
     * @param element Is the ball which needs to be moved.
     * @param dir Is the direction in which the ball needs to be moved.
     * @return A cloned board on which the given move has been executed on is
     *         returned.
     */
    private Abalone executeMove(Ball element, Direction dir) {
        Abalone nextState;
        try {
            nextState = clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
        if (nextState.isMovePossible(element, dir)) {
            int row = element.getRow();
            int diag = element.getDiag();
            nextState.moveBall(row, diag, dir);

            Player probableNext = next.getOpponent();
            if (nextState.isPlayerMovePossible(probableNext)) {
                nextState.toggleNext();
            }
            return nextState;
        } else {
            return null;
        }
    }

    private boolean isPlayerMovePossible(Player player) {
        for (Ball element : getPlayerBallList(player)) {
            if (isBallMovePossible(element)) {
                return true;
            }
        }
        return false;
    }

    private boolean isBallMovePossible(Ball cell) {
        for (Direction dir : Direction.values()) {
            if (isMovePossible(cell, dir)) {
                return true;
            }
        }
        return false;
    }

    private boolean isMovePossible(Ball cell, Direction dir) {
        Ball ball = cell;
        Color self = ball.getColor();
        int selfBallCount = 0;
        int opponentBallCount = 0;

        // counts the amount onw consecutive balls in the given direction
        while (ball != null && ball.getColor() == self) {
            ++selfBallCount;
            ball = getNext(ball.getRow(), ball.getDiag(), dir);
        }

        // counts the amount opponent consecutive balls in the given direction
        while (ball != null && ball.getColor() == self.getOpponentColor()) {
            ++opponentBallCount;
            ball = getNext(ball.getRow(), ball.getDiag(), dir);
        }

        // check that no own ball follows the consecutive opponent balls
        if (ball != null && ball.getColor() == self) {
            return false;
        } else {
            return (selfBallCount > opponentBallCount);
        }
    }

    private void moveBall(int row, int diag, Direction dir) {
        boolean keepMoving = true;
        Ball toMove = fillWithEmpty(row, diag);
        while (keepMoving) {
            Ball next = getNext(toMove.getRow(), toMove.getDiag(), dir);
            if (toMove.isEmpty() || next == null) {
                removeFromList(toMove);
                keepMoving = false;
            } else {
                toMove.moveCoordinates(dir);
                insertArray(toMove);
                toMove = next;
            }
        }
    }

    private void removeFromList(Ball ball) {
        if (ball.isHuman()) {
            humanBalls.remove(ball);
        } else if (ball.isMachine()) {
            machineBalls.remove(ball);
        }
    }

    private void toggleNext() {
        if (getNextPlayer().isMachine()) {
            next = Player.HUMAN;
        } else {
            next = Player.MACHINE;
        }
    }

    private Ball getNext(int row, int diag, Direction dir) {
        int rowTo = Utility.getNextRowCoord(row, dir);
        int diagTo = Utility.getNextDiagCoord(diag, dir);
        if (isValidPosition(rowTo, diagTo)) {
            return board[rowTo][diagTo];
        } else {
            return null;
        }
    }

    private LinkedList<Ball> getPlayerBallList(Player player) {
        LinkedList<Ball> playerBalls;
        if (player.isHuman()) {
            playerBalls = humanBalls;
        } else {
            playerBalls = machineBalls;
        }
        return playerBalls;
    }

    private double evaluateBallCount() {
        return getNumberOfBalls(getHumanColor().getOpponentColor())
                - (1.5 * getNumberOfBalls(getHumanColor()));
    }

    private double evaluateCentricity() {
        double evaluateHuman = 0;
        double evaluateMachine = 0;
        for (Ball element : humanBalls) {
            evaluateHuman += getCentricity(element);
        }
        for (Ball element : machineBalls) {
            evaluateMachine += getCentricity(element);
        }
        return evaluateMachine - (1.5 * evaluateHuman);
    }

    private int getCentricity(Ball cell) {
        int row = cell.getRow();
        int diag1 = cell.getDiag();
        int diag2 = row - diag1 + Math.floorDiv(size, 2);
        int delta1 = Math.min(row, size - row - 1);
        int delta2 = Math.min(diag1, size - diag1 - 1);
        int delta3 = Math.min(diag2, size - diag2 - 1);
        return Math.min(Math.min(delta1, delta2), delta3);
    }

    private double winsAtDepth(int depth, Player player) {
        if (isGameOver() && getWinner() == player) {
            return DETERMINE_WIN_CONSTANT / ((double) depth);
        } else {
            return 0;
        }
    }

    private double gameOverAtLevel(int depth) {
        return winsAtDepth(depth, Player.MACHINE)
                - (1.5 * winsAtDepth(depth, Player.HUMAN));
    }

    /**
     * The evaluateSituation method provides an easy way of evaluating the
     * current state of an {@link Abalone} object out of the perspective of
     * the computer player.
     *
     * The method is normally used in a tree to decide, which board is more
     * beneficial for the computer than another.
     *
     * @param depth The depth is the depth within the tree the board is at.
     *              The depth is {@code 0} for the root node.
     * @return A double evaluation of the board is returned. The higher the
     *         evaluation the better the board for the computer player.
     */
    private double evaluateSituation(int depth) {
        return (size * evaluateBallCount()) + evaluateCentricity()
                + gameOverAtLevel(depth);
    }

    /**
     * The getBestMove method is called when the machine player needs know
     * the next best possible move it could make. It first generates a tree
     * and uses a min-max-algorithm to evaluate the Abalone boards.
     *
     * It should only be called when it is the machine players turn.
     *
     * @return The most beneficial board for the machine is returned.
     */
    private Board getBestMove() {
        Node root = new Node(this);
        generateTree(root, level);
        updateMinMaxEvaluation(root);
        return Utility.max(root.getChildren()).getState();
    }

    /**
     * The generateTree method is a recursive method, which generates a game
     * tree of a given height and adds it to a Node. The game tree inherits
     * all the possible player moves which can be executed. It is supposed to
     * start with all the machine move first and then, in the next level of
     * the tree executes all possible moves of the next player.
     *
     * @param parent Is the parent node to which the resulting children are
     *               added to result in a tree. For the initial call of the
     *               method it is usually the root {@link Node}.
     * @param height Is the resulting height of the tree. For the initial
     *               call of the method it is usually the current game level.
     */
    private static void generateTree(Node parent, int height) {
        LinkedList<Node> children = new LinkedList<>();
        if (parent.getState().isGameOver()) {
            children = null;
        } else {
            Abalone parentState = (Abalone) parent.getState();
            Player parentNext = parentState.next;
            for (Ball element : parentState.getPlayerBallList(parentNext)) {
                for (Direction dir : Direction.values()) {
                    Abalone nextState = parentState.executeMove(element, dir);

                    // if move was possible, create child node
                    if (nextState != null) {
                        int currentDepth = nextState.level - height + 1;
                        double eval = nextState.evaluateSituation(currentDepth);
                        Node child = new Node(nextState, eval);
                        children.add(child);

                        // create new tree for the child if necessary
                        if (currentDepth < nextState.level) {
                            int nextHeight = height - 1;
                            generateTree(child, nextHeight);

                        }
                    }
                }
            }
        }
        parent.setChildren(children);
    }

    /**
     * The updateMinMaxEvaluation method is called only once on the completely
     * build tree. It utilizes the min-max-algorithm for updating the
     * evaluation values in the tree.
     *
     * The min-max-algorithm adds the greatest evaluation of its children to
     * a parent node if the next player of the state is the machine, and
     * respectively the smallest if the next player is human, hereby assuming
     * that the human would always make the most hostile move that could
     * possibly be done.
     *
     * @param parent Is the node for which an update of the evaluation is to be
     *             made. For the initial call of the method it is usually the
     *             root node of the tree.
     */
    private static void updateMinMaxEvaluation(Node parent) {
        LinkedList<Node> children = parent.getChildren();
        if (children != null && !children.isEmpty()) {
            for (Node child : children) {
                updateMinMaxEvaluation(child);
            }
            double childEval;
            if (parent.getState().getNextPlayer().isMachine()) {
                childEval = Utility.max(children).getEvaluation();
            } else {
                childEval = Utility.min(children).getEvaluation();
            }
            parent.setEvaluation(parent.getEvaluation() + childEval);
        }
    }
}