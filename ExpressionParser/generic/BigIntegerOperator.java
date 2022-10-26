package expression.generic;

import java.math.BigInteger;

public class BigIntegerOperator implements Operator<BigInteger> {
    @Override
    public BigInteger getConst(Number num) {
        return BigInteger.valueOf(num.longValue());
    }

    @Override
    public BigInteger Add(BigInteger left, BigInteger right) {
        return left.add(right);
    }

    @Override
    public BigInteger Subtract(BigInteger left, BigInteger right) {
        return left.subtract(right);
    }

    @Override
    public BigInteger Divide(BigInteger left, BigInteger right) {
        return left.divide(right);
    }

    @Override
    public BigInteger Multiply(BigInteger left, BigInteger right) {
        return left.multiply(right);
    }

    @Override
    public BigInteger Min(BigInteger left, BigInteger right) {
        return left.compareTo(right) <= 0 ? left : right;
    }

    @Override
    public BigInteger Max(BigInteger left, BigInteger right) {
        return left.compareTo(right) >= 0 ? left : right;
    }

    @Override
    public BigInteger Count(BigInteger num) {
       return BigInteger.valueOf(num.bitCount());
    }

    @Override
    public BigInteger Negate(BigInteger num) {
        return num.negate();
    }
}
