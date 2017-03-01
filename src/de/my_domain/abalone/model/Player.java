package de.my_domain.abalone.model;

/**
 * The Player Enum.
 *
 * The Player enum describes the players which participate in a game which
 * implements the interface {@link Board}.
 */
public enum Player {

    /**
     * Represents the human player.
     */
    HUMAN,

    /**
     * Represents the machine player.
     */
    MACHINE;

    /**
     * The isHuman method determines if the a player is the human.
     *
     * @return {@code true}: player is human
     *         {@code false}: player is machine
     */
    public boolean isHuman() {
        return this == HUMAN;
    }

    /**
     * The isMachine method determines if the a player is the machine.
     *
     * @return {@code true}: player is machine
     *         {@code false}: player is human
     */
    public boolean isMachine() {
        return this ==  MACHINE;
    }

    /**
     * The getOpponent method returns the opponent player of {@code this}.
     *
     * @return {@code Player.MACHINE} if {@code this} is {@code Player.HUMAN}
     *         {@code Player.HUMAN} if {@code this} is {@code Player.MACHINE}
     * @throws IllegalArgumentException in any other case.
     */
    public Player getOpponent() {
        switch (this) {
            case HUMAN:
                return Player.MACHINE;
            case MACHINE:
                return Player.HUMAN;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * The toString method overrides the toString method of the
     * {@link Object} class.
     *
     * @return {@code "HUMAN"} if the player is {@code HUMAN}
     *         {@code "MACHINE"} if the player is {@code MACHINE}
     */
    @Override
    public String toString() {
        return name();
    }
}
