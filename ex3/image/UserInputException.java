package image;

/**
 * The UserInputException class is a custom exception that extends Exception.
 * It is thrown when there is an error related to user input.
 *
 *  @author Emmanuelle Schnitzer
 *  @author Amit Moses
 */
public class UserInputException extends Exception {

    /**
     * Constructs a new UserInputException with the specified detail message.
     *
     * @param message the detail message
     */
    public UserInputException(String message) {
        super(message);
    }
}