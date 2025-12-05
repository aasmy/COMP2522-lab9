package ca.bcit.comp2522.lab9;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

/**
 * Runs the Lucky Vault: Country game.
 *
 * This class manages the game loop, selects the secret country,
 * gets user guesses, tracks attempts, checks correctness, updates
 * the high score, and logs the session.
 *
 * @author Abdullah Asmy
 * @author Indy Grewel
 *
 * @version 1.0
 */
public final class Game
{
    private static final int INITIAL_ATTEMPTS = 0;
    private static final int INITIAL_INDEX = 0;
    private static final int INDEX_INCREMENT = 1;
    private static final int STRING_BUILDER_CAPACITY = 64;

    private static final String COUNTRIES_FILE_PATH = "data/countries.txt";
    private static final String HIGH_SCORE_FILE_PATH = "data/highscore.txt";
    private static final String LOG_DIRECTORY_PATH = "data/logs";

    private static final String EXIT_COMMAND = "QUIT";

    private final WordList wordList;
    private final HighScoreService highScoreService;
    private final LoggerService loggerService;

    private final Random randomGenerator;
    private final Scanner userInputScanner;

    /**
     * Builds a Game instance and initializes all required services.
     */
    public Game()
    {
        final Path countriesPath;
        final Path highScorePath;
        final Path logDirectoryPath;

        countriesPath = Path.of(COUNTRIES_FILE_PATH);
        highScorePath = Path.of(HIGH_SCORE_FILE_PATH);
        logDirectoryPath = Path.of(LOG_DIRECTORY_PATH);

        wordList = new WordList(countriesPath);
        highScoreService = new HighScoreService(highScorePath);
        loggerService = new LoggerService(logDirectoryPath);

        randomGenerator = new Random();
        userInputScanner = new Scanner(System.in, StandardCharsets.UTF_8);
    }

    /**
     * Starts the main game loop.
     */
    public void start()
    {
        final String secretCountry;
        final int secretLength;
        final int storedHighScore;
        final StringBuilder logBuilder;


        int attempts;
        boolean gameFinished;

        secretCountry = pickRandomCountry();
        secretLength = secretCountry.length();
        storedHighScore = highScoreService.readHighScore();
        attempts = INITIAL_ATTEMPTS;
        logBuilder = new StringBuilder(STRING_BUILDER_CAPACITY);

        printHeader(secretLength, storedHighScore);

        gameFinished = false;
        while (!gameFinished)
        {
            System.out.print("Your guess: ");

            final String guess;
            guess = userInputScanner.nextLine().trim();


            if (guess.isEmpty())
            {
                System.out.println("Empty guess. Try again.");
                appendLogLine(logBuilder, guess, "empty");
                continue;
            }

            if (guess.equalsIgnoreCase(EXIT_COMMAND))
            {
                System.out.println("Bye!");
                appendLogLine(logBuilder, guess, "quit");
                gameFinished = true;
                continue;
            }

            attempts = attempts + INDEX_INCREMENT;

            if (guess.length() != secretLength)
            {
                System.out.println("Wrong length (" +
                                    guess.length() +
                                    "). Need " +
                                    secretLength + ".");

                appendLogLine(logBuilder, guess, "wrong_length");
                continue;
            }

            if (guess.equalsIgnoreCase(secretCountry))
            {
                System.out.println("Correct in " +
                                    attempts +
                                    " attempts! Word was: " +
                                    secretCountry);

                appendLogLine(logBuilder, guess, "CORRECT in " + attempts);

                if (storedHighScore == Integer.MAX_VALUE ||
                    attempts < storedHighScore)
                {
                    System.out.println("NEW BEST for COUNTRY mode!");
                    highScoreService.writeHighScore(attempts);
                }

                gameFinished = true;
            }
            else
            {
                final int matchCount;
                matchCount = countMatches(secretCountry, guess);

                System.out.println("Not it. " + matchCount +
                                    " letter(s) correct (right position)."
                );

                appendLogLine(logBuilder, guess, "matches=" + matchCount);
            }
        }

        loggerService.writeLogEntry(logBuilder.toString());
    }

    /**
     * Selects and returns one random country from the loaded list.
     *
     * @return a randomly chosen country name
     */
    private String pickRandomCountry()
    {
        final int size;
        final int index;

        size = wordList.getCountries().size();
        index = randomGenerator.nextInt(size);

        return wordList.getCountries().get(index);
    }

    /**
     * Prints the initial game information to the console,
     * including the secret word length and the stored high score.
     *
     * @param secretLength    the length of the chosen country
     * @param storedHighScore the previously saved best score
     */
    private void printHeader(final int secretLength,
                             final int storedHighScore)
    {
        System.out.println("LUCKY VAULT - COUNTRY MODE. Type QUIT to exit.");
        System.out.println("Secret word length: " + secretLength);

        if (storedHighScore == Integer.MAX_VALUE)
        {
            System.out.println("Current best: —");
        }
        else
        {
            System.out.println("Current best: " + storedHighScore + " attempts");
        }
    }



    /**
     * Counts how many characters match between the secret word
     * and the user's guess in the same letter positions.
     *
     * @param secret the word the user is trying to guess
     * @param guess  the user's current guess
     *
     * @return the number of matching characters in the same index
     */
    private int countMatches(final String secret,
                             final String guess)
    {
        final String lowerSecret;
        final String lowerGuess;
        int index;
        int matches;
        final int totalLength;

        lowerSecret = secret.toLowerCase(Locale.ROOT);
        lowerGuess = guess.toLowerCase(Locale.ROOT);
        index = INITIAL_INDEX;
        matches= INITIAL_INDEX;
        totalLength = lowerSecret.length();

        while (index < totalLength)
        {
            if (lowerSecret.charAt(index) == lowerGuess.charAt(index))
            {
                matches = matches + INDEX_INCREMENT;
            }

            index = index + INDEX_INCREMENT;
        }

        return matches;
    }

    /**
     * Appends a formatted log entry containing the guess
     * and its outcome to the provided StringBuilder.
     *
     * @param builder the builder storing the session log
     * @param guess   the user's guess
     * @param outcome the evaluation of that guess
     */
    private void appendLogLine(final StringBuilder builder,
                               final String        guess,
                               final String        outcome)
    {
        builder.append(guess)
                .append(" | ")
                .append(outcome)
                .append(System.lineSeparator());
    }
}
