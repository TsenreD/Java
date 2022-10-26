package expression.generic;


public class IntegerOperator implements Operator<Integer>{
    @Override
    public Integer getConst(Number num) {
        return num.intValue();
    }

    @Override
    public Integer Add(Integer left, Integer right) {
        return left + right;
    }

    @Override
    public Integer Subtract(Integer left, Integer right) {
        return left - right;
    }

    @Override
    public Integer Divide(Integer left, Integer right) {
        return left / right;
    }

    @Override
    public Integer Multiply(Integer left, Integer right) {
        return left * right;
    }

    @Override
    public Integer Min(Integer left, Integer right) {
        return Integer.min(left, right);
    }

    @Override
    public Integer Max(Integer left, Integer right) {
        return Integer.max(left, right);
    }

    @Override
    public Integer Count(Integer num) {
        return Integer.bitCount(num);
    }

    @Override
    public Integer Negate(Integer num) {
        return (-1)*num;
    }
}
