package expression;

import expression.generic.Operator;

import java.math.BigDecimal;

public class Variable<T> implements MainExpression<T> {
    String var;

    public Variable(String var) {
        this.var = var;
    }

    @Override
    public int evaluate(int num) {
        return num;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return switch (var) {
            case "x" -> evaluate(x);
            case "y" -> evaluate(y);
            case "z" -> evaluate(z);
            default -> throw new UnsupportedOperationException("This operation is not supported");
        };
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
    public T SmartEval(T x, T y, T z, Operator<T> visitor) {
        return switch (var) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new UnsupportedOperationException("This operation is not supported");
        };
    }

    @Override
    public String toString() {
        return var;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Variable && ((Variable) obj).var.equals(this.var);
    }

    @Override
    public int hashCode() {
        return var.hashCode();
    }

//    @Override
//    public BigDecimal evaluate(BigDecimal x) {
//        return x;
//    }

}
