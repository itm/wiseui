package eu.wisebed.wiseui.shared.common;

/**
 * Utility class for common precondition checks.
 * <p/>
 * The class provides two kinds of checks: Argument checks and general checks.
 * The difference is, that argument checks throw an {@link IllegalArgumentException},
 * while general checks throw a {@link RuntimeException}.
 *
 * @author Soenke Nommensen
 */
public class Preconditions {

    /**
     * @param expression Boolean expression, which shall be tested.
     * @param message    Error message
     * @throws IllegalArgumentException
     */
    public static void checkArgument(final boolean expression, final String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * @param argument Object reference, which shall be tested for Null.
     * @param message  Error message
     */
    public static void notNullArgument(final Object argument, final String message) {
        checkArgument(argument != null, message);
    }

    /**
     * @param stringArgument String reference, which shall be tested for Null or empty.
     * @param message        Error message
     */
    public static void notNullOrEmptyArgument(final String stringArgument, final String message) {
        checkArgument(stringArgument != null && !stringArgument.isEmpty(), message);
    }

    /**
     * @param expression Boolean expression, which shall be tested.
     * @param message    Error message
     * @throws RuntimeException
     */
    public static void check(final boolean expression, final String message) {
        if (!expression) {
            throw new RuntimeException(message);
        }
    }

    /**
     * @param reference Object reference, which shall be tested for Null.
     * @param message   Error message
     * @throws RuntimeException
     */
    public static void notNull(final Object reference, final String message) {
        check(reference != null, message);
    }

    /**
     * @param stringReference String reference, which shall be tested for Null or empty.
     * @param message         Error message
     * @throws RuntimeException
     */
    public static void notNullOrEmpty(final String stringReference, final String message) {
        check(stringReference != null && !stringReference.isEmpty(), message);
    }
}
