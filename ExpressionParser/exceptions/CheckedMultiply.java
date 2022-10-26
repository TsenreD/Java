package expression.exceptions;

import expression.*;
import expression.generic.Operator;

import java.math.BigDecimal;

public class CheckedMultiply<T> extends AbstractBinary<T> implements MainExpression<T> {

    public CheckedMultiply(MainExpression<T> left, MainExpression<T> right) {
        super(left, right);
    }

    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    public boolean isDistributive() {
        return true;
    }

    @Override
    public T SmartEval(T x, T y, T z, Operator<T> visitor) {
        return visitor.Multiply(left.SmartEval(x, y, z, visitor), right.SmartEval(x, y, z, visitor));
    }

    @Override
    public String getType() {
        return "*";
    }

    @Override
    public int operate(int left, int right) {
        return OverflowChecker.MulChecker(left, right);
    }

//    @Override
//    public BigDecimal operate(BigDecimal left, BigDecimal right) {
//        throw new UnsupportedExpOperationException("This operation is not supported");
//    }
}
