package expression.exceptions;

import expression.*;
import expression.generic.Operator;

import java.math.BigDecimal;
import java.util.Objects;

public class CheckedNegate<T> implements MainExpression<T> {
    private final MainExpression<T> expression;

    public CheckedNegate(MainExpression<T> expression) {
        this.expression = expression;
    }

//    @Override
//    public BigDecimal evaluate(BigDecimal x) {
//        throw new UnsupportedExpOperationException("This operation is not supported");
//    }

    @Override
    public int evaluate(int x) {
        return (-1) * expression.evaluate(x);
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public boolean isDistributive() {
        return expression.isDistributive();
    }

    @Override
    public T SmartEval(T x, T y, T z, Operator<T> visitor) {
        return visitor.Negate(expression.SmartEval(x, y, z, visitor));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return OverflowChecker.NegateChecker(expression.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return "-" + "(" + expression + ")";
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass().equals(getClass())
                && ((CheckedNegate) obj).expression.equals(expression);
    }


    public String toMiniString() {
        if (expression.getPriority() <= 1 || expression.getClass().equals(getClass())) {
            return "- " + expression.toMiniString();
        }
        return "-" + "(" + expression.toMiniString() + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(this, -1);
    }
}
