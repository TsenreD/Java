package expression.exceptions;

import expression.*;
import expression.generic.Operator;

import java.math.BigDecimal;

public class CheckedSubtract<T> extends AbstractBinary<T> implements MainExpression<T> {

    public CheckedSubtract(MainExpression<T> left, MainExpression<T> right) {
        super(left, right);
    }

    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    public boolean isDistributive() {
        return false;
    }

    @Override
    public T SmartEval(T x, T y, T z, Operator<T> visitor) {
        return visitor.Subtract(left.SmartEval(x, y, z, visitor), right.SmartEval(x, y, z, visitor));
    }

    @Override
    public String getType() {
        return "-";
    }

    @Override
    public int operate(int left, int right) {
        return OverflowChecker.SubChecker(left, right);
    }

//    @Override
//    public BigDecimal operate(BigDecimal left, BigDecimal right) {
//        throw new UnsupportedExpOperationException("This operation is not supported");
//    }
}
