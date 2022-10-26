package expression.exceptions;

import expression.*;
import expression.generic.Operator;

import java.math.BigDecimal;

public class CheckedPow<T> extends AbstractBinary<T> implements MainExpression<T> {
    public CheckedPow(MainExpression<T> left, MainExpression<T> right) {
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
        return "**";
    }

    @Override
    public int operate(int left, int right) {
        int ans = 1;
        if (right < 0) {
            throw new PowAllowedAreaException("Negative second argument in Pow");
        }
        if (ans == left) {
            return ans;
        } else if (ans == -left) {
            return right % 2 == 0 ? 1 : -1;
        }
        if (left == 0) {
            if (right == 0) {
                throw new PowAllowedAreaException("Both arguments are zero in Pow");
            } else {
                return 0;
            }
        }
        for (int i = 0; i < right; i++) {
            ans = OverflowChecker.MulChecker(ans, left);
        }
        return ans;
    }

//    @Override
//    public BigDecimal operate(BigDecimal left, BigDecimal right) {
//        throw new UnsupportedExpOperationException("This operation is not supported");
//    }
}
