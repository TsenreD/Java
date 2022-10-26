package expression;

import expression.generic.Operator;

import java.math.BigDecimal;

public class LeftShift<T> extends AbstractBinary<T> implements MainExpression<T> {

    public LeftShift(MainExpression<T> left, MainExpression<T> right) {
        super(left, right);
    }

    @Override
    public int getPriority() {
        return 5;
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
    public String getType() {
        return "<<";
    }

    @Override
    public int operate(int left, int right) {
        return left << right;
    }

//    @Override
//    public BigDecimal operate(BigDecimal left, BigDecimal right) {
//        return new BigDecimal(operate(left.intValueExact(), right.intValueExact()));
//    }
}
