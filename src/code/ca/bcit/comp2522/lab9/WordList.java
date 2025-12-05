package ca.bcit.comp2522.lab9;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list of country names used by the guessing game.
 *
 * <p>This class loads the words from a text file and stores
 * them in memory as a list of validated strings.</p>
 *
 * @author Abdullah Asmy
 * @author Indy Grewel
 *
 * @version 1.0
 */
public final class WordList
{
    private final List<String> countries;

    /**
     * Constructs a WordList by reading all lines from the specified file.
     *
     * @param filePath the path to the file containing the country list
     *
     * @throws IllegalArgumentException if the path is null, unreadable,
     *                                  or the file contains blank or missing entries
     */
    public WordList(final Path filePath)
    {
        validatePath(filePath);

        final List<String> loadedLines;
        loadedLines = loadFile(filePath);

        validateLines(loadedLines);

        countries = new ArrayList<>(loadedLines);
    }

    /**
     Validates that the provided file path is not null.
     *
     * @param filePath the path to be checked.
     *
     * @throws IllegalArgumentException if the file path is null
     */
    private static void validatePath (final Path filePath)
    {
        if (filePath == null)
        {
            throw new IllegalArgumentException("File path cannot be null.");
        }

    }

    /**
     * Loads lines from the file using UTF-8 encoding.
     *
     * @param filePath the file path to read
     *
     * @return a list of lines
     *
     * @throws IllegalArgumentException if reading fails, the file is empty, or a line is invalid.
     */
    private List<String> loadFile(final Path filePath)
    {
        try
        {
            return Files.readAllLines(filePath, StandardCharsets.UTF_8);
        }
        catch (final IOException e)
        {
            throw new IllegalArgumentException("Failed to read countries file: " +
                                                filePath, e);
        }
    }

    /**
     * Ensures that the loaded lines contain actual country entries.
     *
     * <p>This method checks that:
     * <ul>
     *   <li>the file has at least one line</li>
     *   <li>no line is null</li>
     *   <li>no line is blank or whitespace-only</li>
     * </ul>
     *
     * @param lines the lines loaded from the country file
     *
     * @throws IllegalArgumentException if the file is empty or contains any blank/null line
     */
    private void validateLines(final List<String> lines)
    {
        if (lines == null || lines.isEmpty())
        {
            throw new IllegalArgumentException("Country file has no entries.");
        }

        for (final String line : lines)
        {
            if (line == null)
            {
                throw new IllegalArgumentException("Country file contains a null line.");
            }

            if (line.isBlank())
            {
                throw new IllegalArgumentException("Country file contains a blank or whitespace-only line.");
            }
        }
    }


    /**
     * Returns a defensive copy of the list of countries.
     *
     * @return a list containing all country names
     */
    public List<String> getCountries()
    {
        return new ArrayList<>(countries);
    }
}
