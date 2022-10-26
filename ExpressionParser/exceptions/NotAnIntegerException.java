package expression.exceptions;

public class NotAnIntegerException extends ParsingException {
    public NotAnIntegerException(String message) {
        super(message);
    }
}
