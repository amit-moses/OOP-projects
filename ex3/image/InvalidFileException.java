package image;

import java.io.IOException;

/**
 * The InvalidFileException class is a custom exception that extends IOException.
 * It is thrown when an invalid file is encountered.
 *
 *  @author Emmanuelle Schnitzer
 *  @author Amit Moses
 */
public class InvalidFileException extends IOException {
    private static final String MESSAGE = "Invalid file";

    /**
     * Constructs a new InvalidFileException with a default error message.
     */
    public InvalidFileException() {
        super(MESSAGE);
    }
}