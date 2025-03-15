package ex5.main.valid;

/**
 * Exception class for handling SJavac validation errors.
 * This class extends the Exception class and is used to signal validation errors
 * encountered during the SJavac program execution.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class SJavacException extends Exception {
    /**
     * Constructs a new SJavacException with the specified detail message.
     *
     * @param message the detail message
     */
    public SJavacException(String message) {
        super(message);
    }
}