package de.my_domain.abalone;

import de.my_domain.abalone.gui.GameFrame;
import de.my_domain.abalone.model.Player;

import javax.swing.SwingUtilities;

/**
 * Main.
 *
 * The Main final class in the main class of the AbaloneGUI program. It invokes
 * the Event Dispatcher in which the GUI application will run. It holds the
 * default size, level and first player as constants and has a reference to
 * the {@link GameFrame} window. It also implement the main method.
 */
public final class Main {

    private static final int DEFAULT_SIZE = 9;
    private static final int DEFAULT_LEVEL = 2;
    private static final Player DEFAULT_FIRST = Player.HUMAN;

    private static GameFrame gui;

    /**
     * The Main constructor is explicitly set to private and throws an
     * {@link UnsupportedOperationException} so that it is never called.
     *
     * @throws UnsupportedOperationException when called.
     */
    private Main() {
        throw new UnsupportedOperationException();
    }

    /**
     * The main method which is run as soon as the program is run.
     *
     * It invokes the Event Dispatcher in which the graphic application will
     * run. Within the Event Dispatcher a new {@link GameFrame} object is
     * created. The creation of the {@link GameFrame} object start the view
     * application.
     *
     * @param args Shell input Parameters args are received here.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            gui = new GameFrame(DEFAULT_SIZE, DEFAULT_LEVEL, DEFAULT_FIRST);
        });
    }
}
