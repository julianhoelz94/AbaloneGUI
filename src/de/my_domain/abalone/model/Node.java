package de.my_domain.abalone.model;

import java.util.LinkedList;

/**
 * The Node Wrapper Class.
 *
 * The Node class is used to create a tree of possible moves for the an
 * object of the type {@link Abalone}.
 *
 * It saves a Abalone state, children an evaluation. It also provides an
 * min-max-algorithm in order to score the states, such that the machine
 * knows which move is most beneficial for it. It also provides a method to
 * get a hold of exactly this board.
 */
public class Node implements Comparable<Node> {

    private Board state;
    private LinkedList<Node> children;
    private double evaluation;

    /**
     * This Node constructor returns a new {@link Node} object and sets only
     * its state. The children are set to null
     *
     * It also sets the {@code hasChildrenSet} flag to false, so that
     * the method {@code setChildren()} can be called on the returned
     * object.
     *
     * This constructor is mainly called for constructing a root node.
     *
     * @param state Is a object of the type {@link Abalone} which saves the
     *              current state of the game.
     */
    public Node(Board state) {
        this.state = state;
        this.children = null;
    }

    /**
     * This Node constructor returns a new {@link Node} object and sets
     * its state and an evaluation for the given board. The children are set
     * to {@code null}.
     *
     * It also sets the {@code hasChildrenSet} flag to false, so that
     * the method {@code setChildren()} can be called on the returned
     * object.
     *
     * @param state Is a object of the type {@link Abalone} which saves the
     *              current state of the game.
     * @param evaluation Is an integer value scoring the game. The higher the
     *                   value, the better is the current state of the game
     *                   for the machine.
     */
    public Node(Board state, double evaluation) {
        this.state = state;
        this.evaluation = evaluation;
        this.children = null;
    }

    /**
     * The getState method is the getter method for the {@link Board} object
     * state.
     *
     * @return The current state will be returned.
     */
    public Board getState() {
        return state;
    }

    /**
     * The setChildren method is the getter method of the children of a
     * {@link Node}.
     *
     * @return The children of a {@link Node} are returned as a
     *         {@link LinkedList}.
     */
    public LinkedList<Node> getChildren() {
        return children;
    }

    /**
     * The setChildren method is the setter method of the children of a
     * {@link Node}, such that a single node can become a parent in a tree of
     * nodes.

     * This method is the only way to initialize the attribute {@code children}
     * since every constructor sets mentioned attribute to {@code null}.
     *
     * @param children A LinkedList of objects of the type {@link Node} is
     *                 given to this method, which then sets the list as
     *                 the children of {@code this}.
     */
    public void setChildren(LinkedList<Node> children) {
        this.children = children;
    }

    /**
     * The getEvaluation method is the getter method of the evaluation value
     * saved in the Node.
     *
     * @return The evaluation to a Board saved in the Node is returned.
     */
    public double getEvaluation() {
        return evaluation;
    }

    /**
     * The setEvaluation method is the setter method of the evaluation value
     * saved in the Node.
     *
     * @param evaluation Is the new evaluation value of a node.
     */
    public void setEvaluation(double evaluation) {
        this.evaluation = evaluation;
    }

    /**
     * The compareTo method implements the compareTo method of the interface
     * {@link Comparable}.
     *
     * It compares two objects of the type {@link Node} for its minMaxEvaluation
     * value.
     *
     * @param o Is the {@link Node} object, the {@code this} object is
     *          compared with.
     * @return   The negative integer {@code -1}, {@code 0}, or the positive
     *           integer {@code 1} as the evaluation of {@code this} node is
     *           less than, equal to, or greater than the specified node
     *           {@code o} will be returned.
     */
    @Override
    public int compareTo(Node o) {
        if (this.equals(o)) {
            return 0;
        } else {
            return (int) Math.signum(this.evaluation - o.evaluation);
        }
    }
}
