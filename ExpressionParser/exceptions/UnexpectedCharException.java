package expression.exceptions;

public class UnexpectedCharException extends ParsingException {
    public UnexpectedCharException(String message) {
        super(message);
    }
}
