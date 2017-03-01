package de.my_domain.abalone.gui;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The ButtonPanel Class.
 *
 * The ButtonPanel class is a {@link JPanel}, which is used to display
 * control units to interact with the {@link de.my_domain.abalone.model.Board}
 * game.
 *
 * The ButtonPanel has drop down menus to set the size of the next game as
 * well as the difficulty of the current and the next game. It also provides
 * buttons to create a new game, discarding the old, create an new game,
 * switching to another first player and to quit the game. It additionally
 * provides the owner with methods to ask for the current selected values in
 * the drop down menus.
 */
public class ButtonPanel extends JPanel {

    private static final Integer[] POSSIBLE_SIZE = {7, 9, 11, 13, 15};
    private static final int DEFAULT_SIZE_INDEX = 1;
    private static final Integer[] POSSIBLE_LEVEL = {1, 2, 3};
    private static final int DEFAULT_LEVEL_INDEX = 1;

    /** Is the JFrame the BoardPanel is located in. */
    private GameFrame frame;

    private JButton newButton;
    private JButton switchButton;
    private JButton quitButton;

    private JComboBox<Integer> sizeMenu;
    private JComboBox<Integer> levelMenu;

    /**
     * The ButtonPanel constructor creates a new ButtonPanel object, which
     * holds drop down menus and buttons. The constructor creates the buttons
     * and lays them out perfectly on the ButtonPanel.
     *
     * @param frame The frame is the GameFrame the ButtonPanel is located in.
     */
    public ButtonPanel(GameFrame frame) {
        super();
        this.frame = frame;
        initializeButtons();
        initializeDropDownMenus();
        customizeLayout();
    }

    /**
     * The initializeButtons method creates the new, switch and the
     * quit button and sets their functionality using an
     * {@link java.awt.event.ActionListener}.
     */
    private void initializeButtons() {
        newButton = new JButton("new");
        newButton.addActionListener((e) -> {
            frame.newGame(frame.getModel().getOpeningPlayer());
        });

        switchButton = new JButton("switch");
        switchButton.addActionListener((e) -> {
            frame.newGame(frame.getModel().getOpeningPlayer().getOpponent());
        });

        quitButton = new JButton("quit");
        quitButton.addActionListener((e) -> {
            frame.dispose();
        });
    }

    /**
     * The initializeDropdown method creates the size and level menu and sets
     * their functionality. The level menu immediately sets the level of the
     * game as soon as value has changed using an
     * {@link java.awt.event.ActionListener}.
     */
    private void initializeDropDownMenus() {
        sizeMenu = new JComboBox<>(POSSIBLE_SIZE);
        sizeMenu.setSelectedIndex(DEFAULT_SIZE_INDEX);

        levelMenu = new JComboBox<>(POSSIBLE_LEVEL);
        levelMenu.setSelectedIndex(DEFAULT_LEVEL_INDEX);
        levelMenu.addActionListener((e) -> {
            int newLevel = (int) levelMenu.getSelectedItem();
            frame.getModel().setLevel(newLevel);
        });
    }

    /**
     * The customizeLayout method sets the Layout of the ButtonPanel to a
     * {@link FlowLayout}, then add label fot the drop down menues followed
     * by the menu itself. Finally, it adds the buttons.
     */
    private void customizeLayout() {
        setLayout(new FlowLayout());

        // add dropdown menus
        add(new JLabel("Size:"));
        add(sizeMenu);
        add(new JLabel("Level:"));
        add(levelMenu);

        // add Buttons
        add(newButton);
        add(switchButton);
        add(quitButton);
    }

    /**
     * The getMenuSize method returns the current size which is selected in
     * the size drop down menu. It can be called to know the next size of the
     * board, when a new {@link de.my_domain.abalone.model.Board} object
     * needs to be created.
     *
     * @return The current selected size as an int.
     */
    int getMenuSize() {
        return (int) sizeMenu.getSelectedItem();
    }

    /**
     * The getMenuLevel method returns the current level which is selected in
     * the level drop down menu. It can be called to know the next level of the
     * board, when a new {@link de.my_domain.abalone.model.Board} object
     * needs to be created.
     *
     * @return The current selected level as an int.
     */
    int getMenuLevel() {
        return (int) levelMenu.getSelectedItem();
    }
}
