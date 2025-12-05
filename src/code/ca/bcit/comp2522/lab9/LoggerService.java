package ca.bcit.comp2522.lab9;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Writes simple session logs for the game.
 *
 * <p>Each game session creates a new log file under the logs directory.
 * The log file records basic information such as the secret country,
 * number of attempts, and outcome.</p>
 *
 * @author Abdullah Asmy
 * @author Indy Grewel
 *
 * @version 1.0
 */
public final class LoggerService
{
    private static final String LOG_FILE_PREFIX = "session-";
    private static final String LOG_FILE_EXTENSION = ".txt";

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    private final Path logDirectory;

    /**
     * Creates a LoggerService and validates the log directory.
     *
     * @param directoryPath the directory in which log files will be created
     *
     * @throws IllegalArgumentException if directoryPath is invalid
     */
    public LoggerService(final Path directoryPath)
    {
        validateDirectoryPath(directoryPath);
        logDirectory = directoryPath;
        ensureDirectoryExists();
    }

    /**
     * Validates that the directory path is not null.
     *
     * @param directoryPath the directory path to validate
     *
     * @throws IllegalArgumentException if the directory path is null
     */
    private static void validateDirectoryPath(final Path directoryPath)
    {
        if (directoryPath == null)
        {
            throw new IllegalArgumentException("Log directory path cannot be null.");
        }
    }

    /**
     * Ensures that the log directory exists; creates it if missing.
     *
     * @throws IllegalArgumentException if the directory cannot be created
     */
    private void ensureDirectoryExists()
    {
        try
        {
            if (!Files.exists(logDirectory))
            {
                Files.createDirectories(logDirectory);
            }
        }
        catch (final IOException ioe)
        {
            throw new IllegalArgumentException("Failed to create log directory.", ioe);
        }
    }

    /**
     * Writes a single log entry into a new session log file.
     *
     * <p>The filename includes a timestamp using the format
     * yyyyMMdd-HHmmss to uniquely identify each session.</p>
     *
     * @param content the log content to write
     *
     * @throws IllegalArgumentException if content is null or blank,
     *                                  or if writing to the file fails
     */
    public void writeLogEntry(final String content)
    {
        validateContent(content);

        final Path logFile;
        logFile = createLogFilePath();

        try (BufferedWriter writer =
                     Files.newBufferedWriter(logFile, StandardCharsets.UTF_8))
        {
            writer.write(content);
        }
        catch (final IOException ioe)
        {
            throw new IllegalArgumentException("Failed to write log entry.", ioe);
        }
    }

    /**
     * Validates that content is not null or blank.
     *
     * @param content the content to validate
     *
     * @throws IllegalArgumentException if content is null or blank
     */
    private static void validateContent(final String content)
    {
        if (content == null)
        {
            throw new IllegalArgumentException("Log content cannot be null.");
        }

        if (content.isBlank())
        {
            throw new IllegalArgumentException("Log content cannot be blank or whitespace-only.");
        }
    }

    /**
     * Creates a timestamped log file path inside the log directory.
     *
     * @return the full path to a new log file
     */
    private Path createLogFilePath()
    {
        final String timestamp;
        final String filename;

        timestamp = LocalDateTime.now().format(FORMATTER);
        filename = LOG_FILE_PREFIX + timestamp + LOG_FILE_EXTENSION;

        return logDirectory.resolve(filename);
    }
}
