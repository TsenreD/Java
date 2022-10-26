package expression.exceptions;

public class OverflowChecker {
    public static int AddChecker(int left, int right) {
        if ((left > 0 && right > 0 && (Integer.MAX_VALUE - left < right)) || (left < 0 && right < 0 && (Integer.MIN_VALUE - right > left))) {
            throw new OverflowException("Overflow occurred in Add ");
        }
        return left + right;
    }

    public static int DivideChecker(int left, int right) {
        if (left == Integer.MIN_VALUE && right == -1) {
            throw new OverflowException("Overflow occurred in Divide ");
        }
        if (right == 0) {
            throw new DivisionByZeroException("Division by zero occurred ");
        }
        return left / right;
    }

    public static int SubChecker(int left, int right) {
        if ((left > 0 && right < 0 && (right < left - Integer.MAX_VALUE)) || (left < 0 && right > 0 && (left < Integer.MIN_VALUE + right))
                || (left == 0 && right == Integer.MIN_VALUE)) {
            throw new OverflowException("Overflow occurred in Subtract ");
        }
        return left - right;
    }

    public static int MulChecker(int left, int right) {
        if (left != 0 && right != 0
                && ((right < 0 && left > 0 && right < Integer.MIN_VALUE / left)
                || (right > 0 && left < 0 && left < Integer.MIN_VALUE / right)
                || (right < 0 && left < 0 && right < Integer.MAX_VALUE / left)
                || (right > 0 && left > 0 && right > Integer.MAX_VALUE / left))) {
            throw new OverflowException("Overflow occurred in Multiply ");
        }
        return left * right;
    }

    public static int AbsChecker(int num) {
        return num >= 0 ? num : NegateChecker(num);
    }

    public static int NegateChecker(int num) {
        if (num == Integer.MIN_VALUE) {
            throw new OverflowException("Overflow occurred in Negate ");
        }
        return (-1) * num;
    }
}
