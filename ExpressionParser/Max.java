package expression;

import expression.generic.Operator;

public class Max<T> extends AbstractBinary<T> implements MainExpression<T> {
    public Max(MainExpression<T> left, MainExpression<T> right) {
        super(left, right);
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public boolean isDistributive() {
        return true;
    }

    @Override
    public T SmartEval(T x, T y, T z, Operator<T> visitor) {
        return visitor.Max(left.SmartEval(x, y, z, visitor), right.SmartEval(x, y, z, visitor));
    }

    @Override
    public String getType() {
        return "max";
    }

    @Override
    public int operate(int left, int right) {
        return Integer.max(left, right);
    }

//    @Override
//    public BigDecimal operate(BigDecimal left, BigDecimal right) {
//        return left.multiply(right);
//    }
}
