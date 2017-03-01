package de.my_domain.abalone.gui;

import de.my_domain.abalone.model.Board;
import de.my_domain.abalone.model.Color;
import de.my_domain.abalone.model.Direction;
import de.my_domain.abalone.model.Player;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

/**
 * The BoardPanel Class.
 *
 * The BoardPanel class is a {@link JPanel}. It is used to represent the
 * current state of a {@link Board} game. It hold many {@link GridPanel}, as
 * well as {@link BallPanel} objects to do so. In the lower left and right
 * corner it holds the current ball count of each player.
 *
 * The BoardPanel also offers methods to either completely reconstruct the
 * contents of the panel according to a {@link Board} object - this is
 * usually done when more or less {@link BallPanel} objects are needed - or
 * to mutate the current {@link BallPanel} object. It also implements the
 * action handling of the ball panels within the BoardPanel. So it is able to
 * select or highlight a ball panel. It also provides the user a way to undo
 * the selection completely.
 */
public class BoardPanel extends JPanel {

    /** Is the JFrame the BoardPanel is located in. */
    private GameFrame frame;

    private int size;
    private BallPanel[][] balls;
    private CountPanel humanPanel;
    private CountPanel machinePanel;

    /**
     * Is the BallPanel that is currently selected. {@code null} if no ball
     * is currently selected.
     */
    private BallPanel firstSelected;

    /**
     * The BoardPanel constructor creates a new BoardPanel object, which holds
     * the current representation of an Abalone {@link Board} game. It
     * initialized the BoardPanel, sets the background color and adds
     * BallPanels on itself, each of which represent a slot in the game.
     *
     * @param frame The frame is the GameFrame the BoardPanel is located in.
     */
    public BoardPanel(GameFrame frame) {
        super();
        this.frame = frame;
        this.firstSelected = null;

        // set design
        setOpaque(true);
        setBackground(DesignUtil.BACKGROUND);

        // initialize information
        Board model = frame.getModel();
        size = model.getSize();
        initializeBoard(model);
        setInformation(model);
        customizeLayout(model);
    }

    /**
     * The initializeBoard method is used to create the {@link BallPanel}
     * objects, as well as the {@link CountPanel} object needed to represent
     * the game.
     *
     * @param model Is the model object from which the method knows how many
     *              balls to create and how to call them.
     */
    private void initializeBoard(Board model) {
        balls = new BallPanel[size + 2][size + 2];
        for (int row = -1; row <= size; ++row) {
            for (int diag = -1; diag <= size; ++diag) {
                if (model.isValidTarget(row, diag)) {
                    balls[row + 1][diag + 1] = new BallPanel(row, diag);
                }
            }
        }
        humanPanel = new CountPanel(this);
        machinePanel = new CountPanel(this);
    }

    /**
     * The setInformation method updates the color information of each ball
     * by asking the {@link Board} object model which color each of the slots
     * has. It also sets the information displayed in the {@link CountPanel}
     * object.
     *
     * @param model Is the model object from which the method knows the color
     *              of each ball, as well as the count information.
     */
    void setInformation(Board model) {
        for (int row = -1; row <= size; ++row) {
            for (int diag = -1; diag <= size; ++diag) {
                if (model.isValidPosition(row, diag)) {
                    balls[row + 1][diag + 1].setColor(model.getSlot(row, diag));
                }
            }
        }
        humanPanel.setInformation(frame.getScore(Player.HUMAN), model
                .getHumanColor());
        machinePanel.setInformation(frame.getScore(Player.MACHINE), model
                .getHumanColor().getOpponentColor());
    }

    /**
     * The customizeLayout method first removes all the existing objects from
     * the BoardPanel, then sets the Layout to a {@link GridBagLayout}. Each
     * of the {@link BallPanel} objects created by the initializeBoard method
     * are then added to the {@link GridBagLayout}.
     *
     * Additionally a {@link GridPanel} is assigned to empty room amongst the
     * {@link BallPanel} objects. The {@link GridPanel} exists to design the
     * room in between to {@link BallPanel} objects.
     *
     * Finally, a method is called to add the {@link CountPanel} objects.
     *
     * @param model Is the model object from which the method knows which
     *              cell in the array is a {@link BallPanel}.
     */
    private void customizeLayout(Board model) {
        removeAll();
        setLayout(new GridBagLayout());

        int lastRow = Integer.MIN_VALUE;
        for (int row = -1; row <= size; ++row) {
            for (int diag = -1; diag <= size; ++diag) {
                if (model.isValidTarget(row, diag)) {
                    int gridX = getGridX(row, diag);
                    int gridY = getGridY(row);
                    BallPanel ball = balls[row + 1][diag + 1];
                    addBall(ball, gridX, gridY);
                    ball.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            super.mouseReleased(e);
                            ballClicked(ball);
                        }
                    });

                    // if needed add FillerBall with horizontal strip
                    if (lastRow == row) {
                        addBall(new GridPanel(), gridX - 1, gridY);
                    }
                    lastRow = row;
                }
            }
        }
        customizeCountPanelLayout();
    }

    /**
     * The addBall method is a small helper method, which allows one to add a
     * {@link GridPanel} or a {@link BallPanel} object to a specified
     * position on the BoardPanel.
     *
     * @param ball The {@link GridPanel} object that will be added.
     * @param gridX The x-coordinate the object needs to be added at.
     * @param gridY The y-coordinate the object needs to be added at.
     */
    private void addBall(GridPanel ball, int gridX, int gridY) {
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.fill = GridBagConstraints.BOTH;
        constraint.weightx = 1;
        constraint.weighty = 1;
        constraint.gridx = gridX;
        constraint.gridy = gridY;
        add(ball, constraint);
    }

    /**
     * The getGridX method takes Abalone coordinates and converts them into
     * the x-Coordinate on which the BallPanel is supposed to be in on the
     * Board Panel.
     *
     * @param row The row coordinate the Abalone ball has.
     * @param diag The diag coordinate the Abalone ball has.
     * @return The x-coordinate of the Ball on the grid is returned.
     */
    private int getGridX(int row, int diag) {
        int middle = Math.floorDiv(size, 2);
        return (middle - row  + (2 * diag) + 2);
    }

    /**
     * The getGridY method takes Abalone coordinates and converts them into
     * the y-Coordinate on which the BallPanel is supposed to be in on the
     * Board Panel.
     *
     * @param row The row coordinate the Abalone ball has.
     * @return The y-coordinate of the Ball on the grid is returned.
     */
    private int getGridY(int row) {
        return (size - row);
    }

    /**
     * The customizeCountPanelLayout method adds the human as well as the
     * machine {@link CountPanel} to the BoardPanel. The panels are added in
     * the right respectively left lower corner of the Panel. The width and
     * height of the Panels are twofold of the width respectively
     * height of the BallPanels.
     */
    private void customizeCountPanelLayout() {
        GridBagConstraints humanConstraint = new GridBagConstraints();
        humanConstraint.fill = GridBagConstraints.BOTH;
        humanConstraint.weightx = 1;
        humanConstraint.weighty = 1;
        humanConstraint.gridx = 0;
        humanConstraint.gridy = size;
        humanConstraint.gridheight = 2;
        humanConstraint.gridwidth = 2;
        add(humanPanel, humanConstraint);

        GridBagConstraints machineConstraint = new GridBagConstraints();
        machineConstraint.fill = GridBagConstraints.BOTH;
        machineConstraint.weightx = 1;
        machineConstraint.weighty = 1;
        machineConstraint.gridx = 2 * size + 1;
        machineConstraint.gridy = size;
        machineConstraint.gridheight = 2;
        machineConstraint.gridwidth = 2;
        add(machinePanel, machineConstraint);
    }

    /**
     * The ballClicked method is called when a ball is clicked. As a
     * parameter is gets the clicked {@link BallPanel}. The method first
     * checks if the machine is busy and if appropriated locks the input and
     * informs the user about the lock. Else it checks if a ball has been
     * clicked before. If not it selects given ball, else if makes a move to
     * the clicked ball. If a selected ball is clicked again it is unselected.
     *
     * @param clicked Is the {@link BallPanel} that has been clicked.
     */
    private void ballClicked(BallPanel clicked) {
        if (frame.getModel().getNextPlayer().isHuman()) {
            Color human = frame.getModel().getHumanColor();
            if (frame.getModel().isGameOver()) {
                frame.message("Game is over. Start a new Game first.");
            } else if (firstSelected != null && clicked == firstSelected) {

                // deselect Balls
                ballsToNormalState();
            } else if (firstSelected != null) {
                move(clicked);
            } else if (DesignUtil.abaloneToAwt(human) == clicked.getColor()) {

                // select Balls
                setFirstSelected(clicked);
                highlightPossible(clicked);
            }
        } else {
            frame.message("Machine move in Progress!");
            repaint();
        }
    }

    /**
     * The move method first gets the coordinates of the upcoming move and
     * then deselects the selected ball. It also removes highlighting of all
     * the possible moves.
     *
     * @param ball Is the {@link BallPanel} that has been clicked last.
     */
    private void move(BallPanel ball) {
        int firstRow = firstSelected.getRow();
        int firstDiag = firstSelected.getDiag();
        ballsToNormalState();
        frame.makeMove(firstRow, firstDiag, ball.getRow(), ball.getDiag());
    }

    /**
     * The highlightPossible method adds a ball to the highlighted list if a
     * move from the selected ball to the soon to be highlighted ball can be
     * made by the user.
     *
     * @param ball Is the {@link BallPanel} that has been selected.
     */
    private void highlightPossible(BallPanel ball) {
        Board currentGame = frame.getModel();
        int row = ball.getRow();
        int diag = ball.getDiag();
        for (Direction dir : Direction.values()) {
            int nextRow = row + dir.getRowOffset();
            int nextDiag = diag + dir.getDiagOffset();

            Board next = currentGame.move(row, diag, nextRow, nextDiag);
            if (next != null) {
                balls[nextRow + 1][nextDiag + 1]
                        .setState(BallPanel.HIGHLIGHTED_STATE);
            }
        }
    }

    /**
     * The setFirstSelected method is a setter method for the ball that has
     * been selected as the ball soon to be moved. It also changes the state
     * of the selected ball to a selected state.
     *
     * @param firstSelected Is the {@link BallPanel} which has been selected.
     */
    private void setFirstSelected(BallPanel firstSelected) {
        this.firstSelected = firstSelected;
        this.firstSelected.setState(BallPanel.SELECTED_STATE);
    }

    /**
     * The ballsToNormalState sets all balls saved to the normal state and
     * deselects the firstSelected ball panel by setting the attribute to
     * {@code null}.
     */
    private void ballsToNormalState() {
        if (firstSelected != null) {
            int row = firstSelected.getRow();
            int diag = firstSelected.getDiag();
            for (Direction dir : Direction.values()) {
                int nextRow = row + dir.getRowOffset();
                int nextDiag = diag + dir.getDiagOffset();
                balls[nextRow + 1][nextDiag + 1]
                        .setState(BallPanel.NORMAL_STATE);
            }
            firstSelected.setState(BallPanel.NORMAL_STATE);
            firstSelected = null;
        }
    }
    /**
     * The getFittingFont method returns the optimal {@link Font} for the
     * {@link CountPanel} objects on this BoardPanel. It also ensures that the
     * displayed text will be resized when the count panel is resized and
     * that the text is always readable. e.g that the text displayed is
     * neither too high nor too wide for the
     * panel.
     *
     * @return The font in which the text will be drawn is returned.
     */
    Font getFittingFont() {
        int width = Math.min(humanPanel.getWidth(), machinePanel.getWidth());
        int height = Math.min(humanPanel.getWidth(), machinePanel.getHeight());
        String humanScore = Integer.toString(frame.getScore(Player.HUMAN));
        String machineScore = Integer.toString(frame.getScore(Player.MACHINE));

        // calculate first font size
        Font font = new Font(DesignUtil.FONT, Font.BOLD, height - 1);
        FontMetrics metrics = getFontMetrics(font);
        int maxStringWidth = Math.max(metrics.stringWidth(humanScore), metrics
                .stringWidth(machineScore));

        // recalculate font size if text is to wide for the count panels
        if (maxStringWidth > width) {
            double ratio = (double) height / (double) maxStringWidth;
            int newFontSize = (int) (ratio * (height)) - 1;
            font = new Font(DesignUtil.FONT, Font.BOLD, newFontSize);
        }
        return font;
    }
}
