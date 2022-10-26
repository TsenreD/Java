package expression.generic;

import expression.exceptions.OverflowChecker;

public class CheckedIntegerOperator extends IntegerOperator implements Operator<Integer>{

    @Override
    public Integer Add(Integer left, Integer right) {
        return OverflowChecker.AddChecker(left, right);
    }

    @Override
    public Integer Subtract(Integer left, Integer right) {
        return OverflowChecker.SubChecker(left, right);
    }

    @Override
    public Integer Divide(Integer left, Integer right) {
        return OverflowChecker.DivideChecker(left, right);
    }

    @Override
    public Integer Multiply(Integer left, Integer right) {
        return OverflowChecker.MulChecker(left, right);
    }

    @Override
    public Integer Negate(Integer num) {
        return OverflowChecker.NegateChecker(num);
    }
}
