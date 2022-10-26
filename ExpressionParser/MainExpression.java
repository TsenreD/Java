package expression;

import expression.generic.Operator;

public interface MainExpression <T> extends Expression, TripleExpression {
    int getPriority();

    boolean isDistributive();

    T SmartEval(T x, T y, T z, Operator<T> visitor);
}
