package de.my_domain.abalone.gui;

import de.my_domain.abalone.model.Abalone;
import de.my_domain.abalone.model.Board;
import de.my_domain.abalone.model.Player;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * The GameFrame Class.
 *
 * The GameFrame class is a {@link JFrame}. It is used as the window of a
 * {@link Board} board. It holds a {@link BallPanel} to display the board, as
 * well as a {@link ButtonPanel} to enable the user to communicate with Abalone.
 *
 * It also hold the board logic and does the communication with the
 * {@link Board} interface of the board. For this it offers methods such as a
 * move, so another class can provoke a human as well as a machine move on the
 * {@link Board} model. The GameFrame class also implements methods to send
 * messages to the user via a pop up window, to create a new board
 * automatically reading the values set in the drop down menus of the
 * {@link ButtonPanel}, a method to ask if the machine is doing something and
 * a getter method of the current Board.
 */
public class GameFrame extends JFrame {

    private Board model;
    private BoardPanel board;
    private ButtonPanel buttons;
    private Thread machineMove;

    /**
     * The boolean run is a flag to discourage automatic, none
     * deterministic methods to continue after the board has either been
     * started again or the window has been closed.
     */
    private boolean run;

    /**
     * The GameFrame constructor creates a new JFrame window for the Abalone
     * board. It first creates a new Abalone model, which is saved as a
     * instance variable, then creates the BoardPanel, which represents the
     * Board and the ButtonPanel for changing settings.
     *
     * The Board also has a minimum size which ensures a good representation
     * of the content.
     *
     * @param startingSize The initial size of the Abalone board is given here.
     * @param startingLevel The initial level of the Abalone board is given
     *                      here.
     * @param first The initial player of the Abalone board is given here.
     */
    public GameFrame(int startingSize, int startingLevel, Player first) {
        super(DesignUtil.TITLE);
        this.model = new Abalone(startingSize, startingLevel, first);
        if (first == Player.MACHINE) {
            makeMachineMove();
        }
        board = new BoardPanel(this);
        buttons = new ButtonPanel(this);
        this.run = true;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(500, 300)); // for optimal representation
        customizeLayout();

        // stop machine after disposing frame.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                stopMachineMove(false);
            }
        });

        setVisible(true);
    }

    /**
     * The customizeLayout method sets the layout of the JPanel to a
     * BorderLayout which has the board panel in the center and the button
     * panel, which allows the player to change settings, on the south
     */
    private void customizeLayout() {
        Container content = getContentPane();
        content.setLayout(new BorderLayout());
        content.add(board, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);
        pack();
    }

    /**
     * The getModel method is a getter method for the Board object saved in
     * GameFrame. This method can be used to ask for certain properties of
     * the current board via the board interface.
     *
     * @return The current {@link Board} model is returned.
     */
    Board getModel() {
        return model;
    }

    /**
     * The getScore method allows one to ask for the current ball count of a
     * {@link Player} object.
     *
     * @param player The {@link Player} object of which the ball count needs
     *               to be known.
     * @return  The current ball count which the player has.
     * @throws  IllegalArgumentException if player is not a {@link Player}.
     */
    int getScore(Player player) {
        if (player == Player.HUMAN) {
            return model.getNumberOfBalls(model.getHumanColor());
        } else if (player == Player.MACHINE) {
            return model.getNumberOfBalls(model.getHumanColor()
                    .getOpponentColor());
        } else {
            throw new IllegalArgumentException("Not a player");
        }
    }

    /**
     * The setModel method is an advanced setter method for the {@link Board}
     * attribute model. It takes a {@link Board} object and sets the old
     * model to the new. The method then initiates an repaint on the
     * {@link BoardPanel}.
     *
     * The method must not be called with a board from another size, since it
     * only does a repaint on the balls of the board and does not create a
     * new board with more or less balls.
     *
     * @param model The model, to which the current model is set.
     */
    private void setModel(Board model) {
        if (this.model.getSize() != model.getSize()) {
            throw new IllegalArgumentException();
        }
        this.model = model;
        board.setInformation(model);
    }

    /**
     * The newGame method creates a new board using the size and the level
     * which are selected in the {@link ButtonPanel}. The player variable
     * determines which {@link Player} is allowed to make the first move.
     *
     * @param first The player, who is allowed to make the first move.
     */
    void newGame(Player first) {
        stopMachineMove(true);
        model = new Abalone(buttons.getMenuSize(), buttons.getMenuLevel(),
                first);
        remove(board);
        board = new BoardPanel(this);
        add(board, BorderLayout.CENTER);
        pack();

        if (first.isMachine()) {
            message("Machine is First.");
            makeMachineMove();
        } else {
            message("You are First.");
        }
    }

    /**
     * The award method should only be called if model.isGameOver() == true.
     * It tells the human user of the program which player has won the board.
     */
    private void award() {
        if (!model.isGameOver()) {
            throw new IllegalStateException("Game not over yet!");
        } else if (model.getWinner().isMachine()) {
            message("Sorry! Machine wins.");
        } else {
            message("Congratulations! You won.");
        }
    }

    /**
     * The message method creates a new JOptionPane to send a message to the
     * user of the program via a popup window. The center of the popup window
     * is set to the GameFrame.
     *
     * @param message The message which needs to be displayed.
     */
    void message(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    /**
     * The makeMove method takes care of the human as well as the machine
     * move. After the human move is done it checks whether the board is over,
     * makes a machine move or message the user when it can make another move.
     * It also checks if the human move was not valid and then messages the
     * user.
     *
     * @param firstRow The row coordinate of the Ball the human wants to move.
     * @param firstDiag The diag coordinate of the Ball the human wants to move.
     * @param row The row coordinate the human wants the selected ball to move
     *            to.
     * @param diag The diag coordinate the human wants the selected ball to move
     *            to.
     */
    void makeMove(int firstRow, int firstDiag,
            int row, int diag) {
        Board nextBoard = model.move(firstRow, firstDiag, row, diag);

        if (nextBoard != null) {
            setModel(nextBoard);

            if (nextBoard.isGameOver()) {
                award();
            } else if (nextBoard.getNextPlayer().isMachine()) {
                makeMachineMove();
            } else {
                message("Machine must skip. Your turn again.");
            }
        } else {
            message("Move not possible.");
        }
    }

    /**
     * The makeMachineMove method creates a new Thread in which the machine
     * move can run. After the machine move has stopped it calls the method
     * updateAfterMachineMove in the EventDispatcher, which then takes care
     * of updating the view.
     *
     * The method also checks if a run flag is set to true. if the run flag
     * is set to false, the flag avoids the machine to continue running.
     */
    private void makeMachineMove() {
        machineMove = new Thread(() -> {
            if (run) {
                Board next = model.machineMove();
                SwingUtilities.invokeLater(() -> updateAfterMachineMove(next));
            }
        });
        machineMove.start();
    }

    /**
     * The updateAfterMachineMove method sets the, by the machine calculated,
     * model to be the new model, and then checks if the board is either over
     * of if the machine can run again. It also conducts this move.
     *
     * The method also checks if a run flag is set to true. if the run flag
     * is set to false, the flag avoids the machine to continue running. It
     * also stops the frame from notifying the user automatically while
     * closing the frame.
     *
     * @param nextModel The nextModel {@link Board} is the the new, by the
     *                  machine calculated, model.
     */
    private void updateAfterMachineMove(Board nextModel) {
        setModel(nextModel);
        if (nextModel.isGameOver() && run) {
            award();
        } else if (nextModel.getNextPlayer().isMachine() && run) {
            message("You must skip. Machine moves again.");
            makeMachineMove();
        }
    }

    /**
     * The stopMachineMove method first sets the run flag to false in order
     * to avoid that the updateAfterMachineMove method can start another
     * machine thread while ending the current thread. Then it ends the
     * machine move using the deprecated method {@link Thread}.stop(). This
     * is the only option to prematurely end the machine thread before it
     * ends itself, since an interrupted flag could only be checked by the
     * machine move either before the calculation has begun or at the time, when
     * the thread would have quit anyway.
     *
     * @param runAgain The runAgain must be set to {@code true} if the
     *                 current session needs to be continued with another game,
     *                 and to {@code false} if the program is determined to
     *                 end immediately.
     */
    @SuppressWarnings("deprecation")
    private void stopMachineMove(boolean runAgain) {

        // set run to false to avoid another start of a machine move
        run = false;
        if ((machineMove != null) && machineMove.isAlive()) {
            machineMove.stop();
        }

        // set run to runAgain if the board should be able to continue
        run = runAgain;
    }
}
