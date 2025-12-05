package ca.bcit.comp2522.lab9;

/**
 * Entry point for the Lucky Vault - Country Edition game.
 * Creates and starts the Game.
 *
 * @author Abdullah Asmy
 * @version 1.0
 */
public class Main
{
    /**
     * Starts the game.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(final String[] args)
    {
        final Game game;
        game = new Game();

        game.start();
    }
}
