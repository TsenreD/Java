package expression.exceptions;

public class MissingArgumentException extends ParsingException{
    public MissingArgumentException(String message) {
        super(message);
    }
}
