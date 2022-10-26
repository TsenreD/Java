package expression.parser;

import expression.exceptions.MissingArgumentException;
import expression.exceptions.UnexpectedCharException;

public class StringSource implements ExpressionSource {
    private final String data;
    private int pos;

    public StringSource(final String data) {
        this.data = data;
    }

    @Override
    public boolean hasNext() {
        return pos < data.length();
    }

    @Override
    public char next() {
        return data.charAt(pos++);
    }

    @Override
    public int getPos() {
        return pos;
    }

    @Override
    public void setPrev() throws MissingArgumentException {
        if (pos <= 1) {
            throw new MissingArgumentException("Expected a beginning of expression at pos 1," +
                    "got operator instead");
        }
        pos -= 2;
    }

    @Override
    public void setPos(int pos) {
        assert pos > 0;
        this.pos = pos;
    }

    @Override
    public UnexpectedCharException error(final String message) {
        return new UnexpectedCharException(pos + ": " + message);
    }
}