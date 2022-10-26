package expression;

import expression.generic.Operator;

import java.math.BigDecimal;

public class LeadingZeroes<T> implements MainExpression<T> {
    MainExpression<T> expression;

    public LeadingZeroes(MainExpression<T> expression) {
        this.expression = expression;
    }

//    @Override
//    public BigDecimal evaluate(BigDecimal x) {
//        return null;
//    }

    @Override
    public int evaluate(int x) {
        return Integer.numberOfLeadingZeros(expression.evaluate(x));
    }

    @Override
    public int getPriority() {
        return 1;
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
    public int evaluate(int x, int y, int z) {
        return Integer.numberOfLeadingZeros(expression.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return "l0" + "(" + expression.toString() + ")";
    }

    @Override
    public String toMiniString() {
        return "l0" + (expression.getPriority() <= 1 ? " " : "(") + expression.toMiniString()
                + (expression.getPriority() <= 1 ? "" : ")");
    }

}
