package ex5.main.valid;

/**
 * Functional interface representing a predicate (boolean-valued function) that can throw an exception.
 * This interface is intended to be used in lambda expressions or method references where a checked exception
 * might be thrown.
 *
 * @author Emmanuelle Schnitzer
 * @autor Amit Moses
 */
@FunctionalInterface
interface ThrowingPredicate {

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param line the input argument
     * @return {@code true} if the input argument matches the predicate, otherwise {@code false}
     * @throws SJavacException if an exception occurs during the evaluation
     */
    boolean test(String line) throws SJavacException;
}