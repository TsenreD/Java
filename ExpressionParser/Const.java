package expression;

import expression.generic.Operator;

import java.math.BigDecimal;
import java.util.Objects;

public class Const<T> implements MainExpression<T> {
    private Number val;

    public Const(int val) {
        this.val = val;
    }

    public Const(BigDecimal bd) {
        this.val = bd;
    }

    @Override
    public int evaluate(int num) {
        return val.intValue();
    }


    @Override
    public int evaluate(int x, int y, int z) {
        return evaluate(x);
    }


    @Override
    public int getPriority() {
        return 0;
    }


    @Override
    public boolean isDistributive() {
        return true;
    }

    @Override
    public T SmartEval(T x, T y, T z, Operator<T> operator) {
        return operator.getConst(val);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Const && val.equals(((Const<?>) obj).val);
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(val);
    }

//    @Override
//    public BigDecimal evaluate(BigDecimal x) {
//        return bd;
//    }

    @Override
    public String toMiniString() {
        return MainExpression.super.toMiniString();
    }
}
