package expression.exceptions;

import expression.*;
import expression.parser.*;

import java.util.Set;

public class ExpressionParser<T> extends BaseParser implements TripleParser {
    private static final Set<String> variableNames = Set.of(
            "x", "y", "z"
    );

    private MainExpression<T> parseMinMax() throws ParsingException {
        skipWhitespace();
        MainExpression<T> expression = parseUnary();
        if (eof()) {
            return expression;
        }
        skipWhitespace();
        while(test("min") || test("max")) {
            if(test("min")) {
                take(3);
                expression = new Min<T>(expression, parseUnary());
            } else {
                take(3);
                expression = new Max<T>(expression, parseUnary());
            }
            skipWhitespace();
        }
        return expression;
    }

    private MainExpression<T> parseLogPow() throws ParsingException {
        skipWhitespace();
        MainExpression<T> expression = parseMinMax();
        skipWhitespace();
        if (eof()) {
            return expression;
        }
        while (test('*') || test('/')) {
            if (test('*')) {
                take();
                if (test('*')) {
                    take();
                    expression = new CheckedPow<T>(expression, parseUnary());
                    skipWhitespace();
                } else {
                    setPrev();
                    return expression;
                }
            } else if (test('/')) {
                take();
                if (test('/')) {
                    take();
                    expression = new CheckedLog<T>(expression, parseUnary());
                    skipWhitespace();
                } else {
                    setPrev();
                    return expression;
                }
            }
        }
        skipWhitespace();
        return expression;
    }

    private MainExpression<T> parseMulDiv() throws ParsingException {
        skipWhitespace();
        MainExpression<T> left = parseLogPow();
        skipWhitespace();
        if (eof()) {
            return left;
        }
        while (test('*') || test('/')) {
            if (test('*')) {
                take();
                left = new CheckedMultiply<T>(left, parseLogPow());
            } else if (test('/')) {
                take();
                left = new CheckedDivide<T>(left, parseLogPow());
            }
        }
        skipWhitespace();
        return left;
    }

    private MainExpression<T> parseSubAdd() throws ParsingException {
        skipWhitespace();
        MainExpression<T> left = parseMulDiv();
        skipWhitespace();
        if (eof()) {
            return left;
        }
        while (test('-') || test('+')) {
            if (test('-')) {
                take();
                left = new CheckedSubtract<T>(left, parseMulDiv());
            } else if (test('+')) {
                take();
                left = new CheckedAdd<T>(left, parseMulDiv());
            }
        }
        skipWhitespace();
        return left;
    }

    private MainExpression<T> parseShifts() throws ParsingException {
        skipWhitespace();
        MainExpression<T> left = parseSubAdd();
        skipWhitespace();
        if (eof()) {
            return left;
        }
        while (test('<') || test('>')) {
            if (test('<')) {
                take();
                expect('<');
                left = new LeftShift<T>(left, parseSubAdd());
            } else if (test('>')) {
                take();
                expect('>');
                if (test('>')) {
                    take();
                    left = new RightArithmeticShift<T>(left, parseSubAdd());
                } else {
                    left = new RightShift<T>(left, parseSubAdd());
                }
            }
        }
        skipWhitespace();
        return left;
    }

    private MainExpression<T> parseUnary() throws ParsingException {
        skipWhitespace();
        if (test('-')) {
            take();
            if (between('0', '9')) {
                return getConst(true);
            } else {
                return new CheckedNegate<T>(parseUnary());
            }
        } else if (test('(')) {
            take();
            MainExpression<T> exp = parseShifts();
            if (!take(')')) {
                throw new BracketsException("Expected ), got " + getCurrent()
                        + " at pos " + getPos());
            }
            return exp;
        } else if (between('0', '9')) {
            return getConst(false);
        } else if (test("l0")) {
            take(2);
            checkUnary(getCurrent(), "l0", getPos());
            return new LeadingZeroes<T>(parseUnary());
        } else if (test("t0")) {
            take(2);
            checkUnary(getCurrent(), "t0", getPos());
            return new TrailingZeroes<T>(parseUnary());
        } else if (test("count")) {
            take(5);
            checkUnary(getCurrent(), "count", getPos());
            return new Count<T>(parseUnary());
        } else if (test("abs")) {
            take(3);
            checkUnary(getCurrent(), "abs", getPos());
            return new CheckedAbs<T>(parseUnary());
        } else if (Character.isAlphabetic(getCurrent())) {
            return getVariable();
        }
        throw new MissingArgumentException("Expected a beginning of an expression argument, got "
                + (eof() ? "end of input" : (getCurrent() + " at position " + (getPos() - 1))));
    }

    private MainExpression<T> getVariable() throws InvalidVariableException {
        StringBuilder name = new StringBuilder();
        while (Character.isAlphabetic(getCurrent())) {
            name.append(take());
        }
        if (!variableNames.contains(name.toString())) {
            throw new InvalidVariableException("Invalid variable occurred at pos "
                    + (getPos() - name.length()) + " : " + name);
        }
        return new Variable<T>(name.toString());
    }

    private MainExpression<T> getConst(boolean negate) throws NotAnIntegerException {
        StringBuilder num = new StringBuilder();
        if (negate) {
            num.append('-');
        }
        while (between('0', '9')) {
            num.append(take());
        }
        try {
            return new Const<T>(Integer.parseInt(num.toString()));
        } catch (NumberFormatException e) {
            throw new NotAnIntegerException("Expected an Integer constant, got " + num
                    + " at pos " + (getPos() - num.length()));
        }
    }

    private void checkUnary(char ch, String name, int pos) throws InvalidUnaryException {
        if (!(Character.isWhitespace(ch) || ch == '(')) {
            throw new InvalidUnaryException("Expected " + name + " with a whitespace or '('," +
                    " got " + (eof() ? "end of input" : (" at pos " + pos)));
        }
    }


    @Override
    public MainExpression<T> parse(String expression) throws ParsingException {
        createSource(new StringSource(expression));
        MainExpression<T> exp = parseShifts();
        if (!eof()) {
            throw new EndOfInputException("Expected end of input, got " + getCurrent()
                    + " at pos " + getPos());
        }
        return exp;
    }
}
