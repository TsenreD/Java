package expression.generic;

public class DoubleOperator implements Operator<Double> {
    @Override
    public Double getConst(Number num) {
        return num.doubleValue();
    }

    @Override
    public Double Add(Double left, Double right) {
        return left + right;
    }

    @Override
    public Double Subtract(Double left, Double right) {
        return left - right;
    }

    @Override
    public Double Divide(Double left, Double right) {
        return left / right;
    }

    @Override
    public Double Multiply(Double left, Double right) {
        return left * right;
    }

    @Override
    public Double Min(Double left, Double right) {
        return Double.min(left, right);
    }

    @Override
    public Double Max(Double left, Double right) {
        return Double.max(left, right);
    }

    @Override
    public Double Count(Double num) {
        return (double) Long.bitCount(Double.doubleToLongBits(num));
    }

    @Override
    public Double Negate(Double num) {
        return (-1) * num;
    }
}
