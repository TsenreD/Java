package expression.exceptions;

import expression.*;
import expression.generic.Operator;

public class CheckedAbs<T> implements MainExpression<T> {
    private final MainExpression<T> expression;

    public CheckedAbs(MainExpression<T> expression) {
        this.expression = expression;
    }

//    @Override
//    public BigDecimal evaluate(BigDecimal x) {
//        throw new UnsupportedExpOperationException("This operation is not supported");
//    }

    @Override
    public int evaluate(int x) {
        int tmp = expression.evaluate(x);
        return OverflowChecker.AbsChecker(tmp);
    }

    @Override
    public int getPriority() {
        return 1;
    }


    @Override
    public boolean isDistributive() {
        return false;
    }

    @Override
    public T SmartEval(T x, T y, T z, Operator<T> visitor) {
        return null;
    }


    @Override
    public int evaluate(int x, int y, int z) {
        int tmp = expression.evaluate(x, y, z);
        if (tmp == Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Overflow in abs");
        }
        return tmp >= 0 ? tmp : tmp * (-1);
    }

    @Override
    public String toString() {
        return "abs" + "(" + expression.toString() + ")";
    }

    @Override
    public String toMiniString() {
        return "abs" + (expression.getPriority() <= 1 ? " " : "(") + expression.toMiniString()
                + (expression.getPriority() <= 1 ? "" : ")");
    }
}
