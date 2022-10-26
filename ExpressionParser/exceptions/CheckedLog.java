package expression.exceptions;

import expression.*;
import expression.generic.Operator;

import java.math.BigDecimal;

public class CheckedLog<T> extends AbstractBinary<T> implements MainExpression<T> {

    public CheckedLog(MainExpression<T> left, MainExpression<T> right) {
        super(left, right);
    }

    @Override
    public int getPriority() {
        return 2;
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
    public String getType() {
        return "//";
    }

    @Override
    public int operate(int left, int right) {
        int ans = 1;
        if (right <= 0 || right == 1) {
            throw new LogAllowedAreaException("Invalid log argument " + this);
        }
        if (left <= 0) {
            throw new LogAllowedAreaException("Invalid log base " + this);
        }
        if (left == 1) {
            return 0;
        }
        int counter = 0;
        while (ans <= left) {
            counter++;
            if (Integer.MAX_VALUE / right < ans) {
                return counter - 1;
            }
            ans *= right;
        }
        return counter - 1;
    }

//    @Override
//    public BigDecimal operate(BigDecimal left, BigDecimal right) {
//        throw new UnsupportedExpOperationException("This operation is not supported");
//    }
}
