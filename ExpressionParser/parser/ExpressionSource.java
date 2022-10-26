package expression.parser;

import expression.exceptions.MissingArgumentException;
import expression.exceptions.UnexpectedCharException;

public interface ExpressionSource {
    boolean hasNext();
    char next();
    int getPos();
    void setPrev() throws MissingArgumentException;
    void setPos(int pos);
    UnexpectedCharException error(final String message);
}
