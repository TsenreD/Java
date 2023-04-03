package itmo.ja.dyagin.implementor;

/**
 * Thrown by {@link Impler} when an error occurred.
 */
public class ImplerException extends Exception {
    /**
     * Creates an {@code ImplerException} with specified error detail message.
     *
     * @param message error detail message.
     */
    public ImplerException(final String message) {
        super(message);
    }

    /** 
     * Creates an {@code ImplerException} with specified error detail message and a cause.
     *
     * @param message error detail message.
     * @param cause error cause.
     */
    public ImplerException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
