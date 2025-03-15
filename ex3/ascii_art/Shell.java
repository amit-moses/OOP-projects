package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image.InvalidFileException;
import image.UserInputException;
import image.WrappedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Shell class is responsible for managing the user interface and interaction
 * for the ASCII art generation.It handles user commands, validates input,
 * and executes the ASCII art conversion process.
 *
 *  @author Emmanuelle Schnitzer
 *  @author Amit Moses
 */
public class Shell {
    private static final char[] CHARSET = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static final int RESOLUTION = 2;
    private static final String START_MESSAGE = ">>> ";
    private static final String EXIT_CMD = "exit";
    private static final String CHARS_CMD = "chars";
    private static final String ADD_CMD = "add";
    private static final String REMOVE_CMD = "remove";
    private static final String RES_CMD = "res";
    private static final String ROUND_CMD = "round";
    private static final String OUTPUT_CMD = "output";
    private static final String UP_CMD = "up";
    private static final String DOWN_CMD = "down";
    private static final String ABS_CMD = "abs";
    private static final String HTML_CMD = "html";
    private static final String CONSOLE_CMD = "console";
    private static final String SPACE_CMD = "space";
    private static final String ALL_CMD = "all";
    private static final String MAKE_ART_CMD = "asciiArt";
    private static final String ROUNDING_ERROR = "Did not change rounding method due to incorrect format.";
    private static final String OUTPUT_ERROR = "Did not change output method due to incorrect format.";
    private static final String CHARSET_SMALL_ERROR = "Did not execute. Charset is too small.";
    private static final String RESOLUTION_ERROR = "Did not change resolution due to incorrect format.";
    private static final String ADD_ERROR = "Did not add due to incorrect format.";
    private static final String REMOVE_ERROR = "Did not remove due to incorrect format.";
    private static final String EXECUTE_ERROR = "Did not execute due to incorrect command.";
    private static final String SET_RESOLUTION = "Resolution set to %d.";
    private static final String FONT_OUTPUT = "Courier New";
    private static final String OUT_FILE = "out.html";
    private static final int VALID_DOWN = 32;
    private static final int VALID_UP = 126;
    private static final int ABS_ROUND = 2;
    private static final int FLOOR_ROUND = 1;
    private static final int CEIL_ROUND = 0;
    private static final int BASE = 2;
    private static final char SPLIT_SEQ = '-';

    /**
     * The AsciiOutput object used for outputting the ASCII art.
     */
    private AsciiOutput outManager;

    /**
     * The AsciiArtAlgorithm object used for converting images to ASCII art.
     */
    private AsciiArtAlgorithm asciiArtAlgorithm;

    /**
     * The character set used for mapping brightness to ASCII characters.
     */
    private final char[] charset;

    /**
     * The main method that initializes the Shell and starts the ASCII art conversion process.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Shell myShell = new Shell(CHARSET);
        myShell.run(args[0]);
    }

    /**
     * Constructs a Shell with the specified character set.
     *
     * @param charset the character set used for mapping brightness to ASCII characters
     */
    public Shell(char[] charset) {
        this.outManager = new ConsoleAsciiOutput();
        this.charset = charset;
    }

    /**
     * Validates the photo at the specified path and initializes the AsciiArtAlgorithm.
     *
     * @param path the path to the image file
     * @throws InvalidFileException if the file is invalid
     */
    private void validPhoto(String path) throws InvalidFileException {
        try {
            Image image = new WrappedImage(path);
            this.asciiArtAlgorithm = new AsciiArtAlgorithm(image, RESOLUTION, this.charset);
        } catch (IOException e) {
            throw new InvalidFileException();
        }
    }

    /**
     * Reads input from the user and returns it as an array of strings.
     *
     * @return an array of strings representing the user input
     */
    private String[] inputFromUser() {
        System.out.print(START_MESSAGE);
        String buffer = KeyboardInput.readLine();
        return buffer.split(" ");
    }

    /**
     * Sets the rounding method for the ASCII art algorithm based on user input.
     *
     * @param input the user input
     * @throws UserInputException if the input is invalid
     */
    private void round(String[] input) throws UserInputException {
        try {
            String command = input[1];
            switch (command) {
                case UP_CMD -> asciiArtAlgorithm.setRound(CEIL_ROUND);
                case DOWN_CMD -> asciiArtAlgorithm.setRound(FLOOR_ROUND);
                case ABS_CMD -> asciiArtAlgorithm.setRound(ABS_ROUND);
                default -> throw new UserInputException(ROUNDING_ERROR);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new UserInputException(ROUNDING_ERROR);
        }
    }

    /**
     * Sets the output method for the ASCII art based on user input.
     *
     * @param input the user input
     * @throws UserInputException if the input is invalid
     */
    private void output(String[] input) throws UserInputException {
        if (input.length <= 1 || (!input[1].equals(HTML_CMD) && !input[1].equals(CONSOLE_CMD))) {
            throw new UserInputException(OUTPUT_ERROR);
        }
        this.outManager = input[1].equals(HTML_CMD) ?
                new HtmlAsciiOutput(OUT_FILE, FONT_OUTPUT) : new ConsoleAsciiOutput();
    }

    /**
     * Runs the ASCII art conversion algorithm and outputs the result.
     *
     * @throws UserInputException if the character set is too small
     */
    private void asciiArt() throws UserInputException {
        if (asciiArtAlgorithm.getSubImgCharMatcher().getCharacters().size() < 2) {
            throw new UserInputException(CHARSET_SMALL_ERROR);
        }
        char[][] out = asciiArtAlgorithm.run();
        this.outManager.out(out);
    }

    /**
     * Prints the characters in the current character set.
     */
    private void chars() {
        List<Character> sortedList = new ArrayList<>(
                asciiArtAlgorithm.getSubImgCharMatcher().getCharacters());
        Collections.sort(sortedList);

        for (char c : sortedList) {
            System.out.print(c + " ");
        }
        System.out.println();
    }

    /**
     * Adds or removes a character from the character set based on user input.
     *
     * @param isAdd true to add the character, false to remove it
     * @param c the character to add or remove
     */
    private void addRemoveHandler(boolean isAdd, char c) {
        if (isAdd) {
            asciiArtAlgorithm.getSubImgCharMatcher().addChar(c);
        } else {
            asciiArtAlgorithm.getSubImgCharMatcher().removeChar(c);
        }
    }

    /**
     * Sets the resolution for the ASCII art based on user input.
     *
     * @param input the user input
     * @throws UserInputException if the input is invalid
     */
    private void res(String[] input) throws UserInputException, IllegalArgumentException {
        if (input.length == 1) {
            System.out.println(asciiArtAlgorithm.getResolution());
            return;
        } else if (input[1].equals(UP_CMD)) {
            asciiArtAlgorithm.setResolution(asciiArtAlgorithm.getResolution() * BASE);
        } else if (input[1].equals(DOWN_CMD)) {
            asciiArtAlgorithm.setResolution(asciiArtAlgorithm.getResolution() / BASE);
        } else {
            throw new UserInputException(RESOLUTION_ERROR);
        }
        System.out.println(String.format(SET_RESOLUTION, asciiArtAlgorithm.getResolution()));
    }

    /**
     * Manages a sequence of characters to add or remove from the character set.
     *
     * @param start the starting character of the sequence
     * @param end the ending character of the sequence
     * @param toAdd true to add the characters, false to remove them
     */
    private void manageSequence(int start, int end, boolean toAdd) {
        int st = Math.min(start, end);
        int en = Math.max(start, end);
        for (int i = st; i <= en; i++) {
            addRemoveHandler(toAdd, (char) i);
        }
    }

    /**
     * Adds or removes characters from the character set based on user input.
     *
     * @param input the user input
     * @param toAdd true to add the characters, false to remove them
     * @throws UserInputException if the input is invalid
     */
    private void addRemove(String[] input, boolean toAdd) throws UserInputException {
        if (1 < input.length) {
            if (input[1].length() == 1 &&
                    input[1].charAt(0) >= VALID_DOWN && input[1].charAt(0) <= VALID_UP) {
                addRemoveHandler(toAdd, input[1].charAt(0));
                return;
            } else if (input[1].equals(ALL_CMD)) {
                manageSequence(VALID_DOWN, VALID_UP, toAdd);
                return;
            }else if (input[1].equals(SPACE_CMD)) {
                addRemoveHandler(toAdd, ' ');
                return;
            } else if (input[1].length() == 3 && input[1].charAt(1) == SPLIT_SEQ &&
                    input[1].charAt(0) >= VALID_DOWN && input[1].charAt(0) <= VALID_UP) {
                manageSequence(input[1].charAt(0), input[1].charAt(2), toAdd);
                return;
            }
        }

        throw new UserInputException(toAdd ? ADD_ERROR : REMOVE_ERROR);
    }

    /**
     * Runs the Shell, processing user commands and executing the corresponding actions.
     *
     * @param imageName the name of the image file to process
     */
    public void run(String imageName) {
        try {
            validPhoto(imageName);
        } catch (InvalidFileException e) {
            System.out.println(e.getMessage());
            return;
        }
        String[] input = inputFromUser();
        while (0 < input.length && !(input[0].equals(EXIT_CMD))) {
            try {
                switch (input[0]) {
                    case CHARS_CMD -> chars();
                    case ADD_CMD -> addRemove(input, true);
                    case REMOVE_CMD -> addRemove(input, false);
                    case RES_CMD -> res(input);
                    case ROUND_CMD -> round(input);
                    case OUTPUT_CMD -> output(input);
                    case MAKE_ART_CMD -> asciiArt();
                    default -> System.out.println(EXECUTE_ERROR);
                }
            } catch (UserInputException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
            input = inputFromUser();
        }
    }
}