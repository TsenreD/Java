package expression.exceptions;

import expression.parser.*;

public class BaseParser {
    private static final char END = '\0';
    private ExpressionSource source;
    private char ch = 0xffff;

    public void createSource(ExpressionSource source) {
        this.source = source;
        take();
    }

    protected char take() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : END;
        return result;
    }

    protected char take(int amount) {
        char cur = ch;
        for(int i =0; i < amount; i++) {
             cur = take();
        }
        return cur;
    }

    protected boolean test(final char expected) {
        return ch == expected;
    }

    protected boolean test(final String expected) {
        assert expected.length() <= 256;
        int prevPos = getPos();
        char prevCh = getCurrent();
        for(int i = 0; i < expected.length(); i++) {
            char curr = expected.charAt(i);
            if(getCurrent() != curr) {
                source.setPos(prevPos);
                ch = prevCh;
                return false;
            }
            take();
        }
        source.setPos(prevPos);
        ch = prevCh;
        return true;
    }

    protected void setPrev() throws MissingArgumentException {
        source.setPrev();
        take();
    }

    protected void skipWhitespace() {
        while (Character.isWhitespace(ch)) {
            take();
        }
    }

    protected boolean take(final char expected) {
        if (test(expected)) {
            take();
            return true;
        }
        return false;
    }

    protected void expect(final char expected) throws UnexpectedCharException {
        if (!take(expected)) {
            throw error("Expected '" + expected + "', found " +
                    (eof() ? "end of input" : ("'" + ch + "'")));
        }
    }

    protected void expect(final String value) throws UnexpectedCharException {
        for (final char c : value.toCharArray()) {
            expect(c);
        }
    }

    protected char getCurrent() {
        return ch;
    }

    protected int getPos() {
        return source.getPos();
    }

    protected boolean eof() {
        return take(END);
    }

    protected UnexpectedCharException error(final String message) {
        return source.error(message);
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }
}
