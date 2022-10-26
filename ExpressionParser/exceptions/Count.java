package expression.exceptions;

import expression.*;
import expression.generic.Operator;

public class Count<T> implements MainExpression<T> {
    private final MainExpression<T> expression;

    public Count(MainExpression<T> expression) {
        this.expression = expression;
    }

//    @Override
//    public BigDecimal evaluate(BigDecimal x) {
//        throw new UnsupportedExpOperationException("This operation is not supported");
//    }

    @Override
    public int evaluate(int x) {
        int tmp = expression.evaluate(x);
        return Integer.bitCount(tmp);
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
        return visitor.Count(expression.SmartEval(x, y, z, visitor));
    }


    @Override
    public int evaluate(int x, int y, int z) {
        int tmp = expression.evaluate(x, y, z);
        return Integer.bitCount(tmp);
    }

    @Override
    public String toString() {
        return "count" + "(" + expression.toString() + ")";
    }

    @Override
    public String toMiniString() {
        return "count" + (expression.getPriority() <= 1 ? " " : "(") + expression.toMiniString()
                + (expression.getPriority() <= 1 ? "" : ")");
    }
}
