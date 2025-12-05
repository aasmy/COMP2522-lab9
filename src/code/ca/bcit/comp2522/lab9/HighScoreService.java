package ca.bcit.comp2522.lab9;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Handles reading and writing the best score for the game.
 *
 * <p>The high score file stores exactly one integer representing
 * the minimum number of attempts a player has taken to guess the country.</p>
 *
 * @author Abdullah Asmy
 * @author Indy Grewel
 *
 * @version 1.0
 */
public final class HighScoreService
{
    private static final int DEFAULT_HIGH_SCORE = Integer.MAX_VALUE;

    private final Path highScoreFile;

    /**
     * Constructs a HighScoreService and ensures the file is set up.
     *
     * @param filePath the path to the high score file
     *
     * @throws IllegalArgumentException if the file path is invalid or
     *                                     the file cannot be initialized
     */
    public HighScoreService(final Path filePath)
    {
        validatePath(filePath);
        highScoreFile = filePath;
        ensureFileExists();
    }

    /**
     * Validates that the provided file path is not null.
     *
     * @param filePath the file path to validate
     *
     * @throws IllegalArgumentException if the file path is null
     */
    private static void validatePath(final Path filePath)
    {
        if (filePath == null)
        {
            throw new IllegalArgumentException("High score file path cannot be null.");
        }
    }

    /**
     * Ensures that the high score file exists; if not, creates it
     * and writes the default high score value.
     *
     * @throws IllegalArgumentException if the file cannot be created
     */
    private void ensureFileExists()
    {
        if (!Files.exists(highScoreFile))
        {
            writeHighScore(DEFAULT_HIGH_SCORE);
        }
    }

    /**
     * Reads and returns the stored high score.
     *
     * @return the stored high score
     *
     * @throws IllegalArgumentException if the file cannot be read or
     *                                     contains invalid data
     */
    public int readHighScore()
    {
        try (BufferedReader reader = Files.newBufferedReader(highScoreFile,
                                                             StandardCharsets.UTF_8))
        {
            final String line;
            line = reader.readLine();

            if (line == null || line.isBlank())
            {
                throw new IllegalArgumentException("High score file is empty or contains a blank line.");
            }

            return parseHighScore(line);
        }
        catch (final IOException ioe)
        {
            throw new IllegalArgumentException("Failed to read high score file.", ioe);
        }
    }

    /**
     * Parses a string into an integer high score.
     *
     * @param text the text to parse
     *
     * @return the parsed integer
     *
     * @throws IllegalArgumentException if the text is not a valid integer
     */
    private int parseHighScore(final String text)
    {
        try
        {
            return Integer.parseInt(text.trim());
        }
        catch (final NumberFormatException nfe)
        {
            throw new IllegalArgumentException("High score file contains a non-numeric value: " + text, nfe);
        }
    }

    /**
     * Writes the given high score to the file.
     *
     * @param score the high score to write
     *
     * @throws IllegalArgumentException if writing fails
     */
    public void writeHighScore(final int score)
    {
        try (BufferedWriter writer =
                     Files.newBufferedWriter(highScoreFile,
                             StandardCharsets.UTF_8))
        {
            writer.write(Integer.toString(score));
        }
        catch (final IOException ioe)
        {
            throw new IllegalArgumentException("Failed to write high score.", ioe);
        }
    }
}
