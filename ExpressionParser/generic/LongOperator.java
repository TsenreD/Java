package expression.generic;

public class LongOperator implements Operator<Long>{
    @Override
    public Long getConst(Number num) {
        return num.longValue();
    }

    @Override
    public Long Add(Long left, Long right) {
        return left + right;
    }

    @Override
    public Long Subtract(Long left, Long right) {
        return left - right;
    }

    @Override
    public Long Divide(Long left, Long right) {
        return left / right;
    }

    @Override
    public Long Multiply(Long left, Long right) {
        return left * right;
    }

    @Override
    public Long Min(Long left, Long right) {
        return Long.min(left, right);
    }

    @Override
    public Long Max(Long left, Long right) {
        return Long.max(left, right);
    }

    @Override
    public Long Count(Long num) {
        return (long) Long.bitCount(num);
    }

    @Override
    public Long Negate(Long num) {
        return (-1)*num;
    }
}
