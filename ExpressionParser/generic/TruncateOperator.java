package expression.generic;


public class TruncateOperator implements Operator<Integer>{
    @Override
    public Integer getConst(Number num) {
        return num.intValue() - num.intValue() % 10;
    }

    @Override
    public Integer Add(Integer left, Integer right) {
        int ans = (left - left % 10) + (right - right % 10);
        return ans - ans % 10;
    }

    @Override
    public Integer Subtract(Integer left, Integer right) {
        int ans = (left - left % 10) - (right - right % 10);
        return ans - ans % 10;
    }

    @Override
    public Integer Divide(Integer left, Integer right) {
        int ans = (left - left % 10) / (right - right % 10);
        return ans  - ans % 10;
    }

    @Override
    public Integer Multiply(Integer left, Integer right) {
        int ans = (left - left % 10) * (right - right % 10);
        return ans - ans % 10;
    }

    @Override
    public Integer Min(Integer left, Integer right) {
        return Integer.min((left - left % 10), (right - right % 10));
    }

    @Override
    public Integer Max(Integer left, Integer right) {
        return Integer.max((left - left % 10), (right - right % 10));
    }

    @Override
    public Integer Count(Integer num) {
        int ans = Integer.bitCount(num);
        return ans - ans % 10;
    }

    @Override
    public Integer Negate(Integer num) {
        int ans = (-1)*num;
        return ans - ans % 10;
    }
}
