package expression;

import java.util.Objects;

public abstract class AbstractBinary<T> implements MainExpression<T> {
    protected MainExpression<T> left;
    protected MainExpression<T> right;

    enum Type {
        LEFT,
        RIGHT
    }

    protected AbstractBinary(MainExpression<T> left, MainExpression<T> right) {
        this.left = left;
        this.right = right;
    }

    public int evaluate(int x) {
        return operate(left.evaluate(x), right.evaluate(x));
    }

    public int evaluate(int x, int y, int z) {
        return operate(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

    //    public BigDecimal evaluate(BigDecimal x) {
//        return operate(left.evaluate(x), right.evaluate(x));
//    }

    @Override
    public String toString() {
        return '(' + left.toString() +
                " " + this.getType() + " " +
                right.toString() + ')';
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass().equals(getClass())
                && ((AbstractBinary) obj).left.equals(this.left)
                && ((AbstractBinary) obj).right.equals(this.right);
    }

    private boolean miniHelper(Type type) {
        int priority = this.getPriority();
        switch (type) {
            case LEFT:
                return left.getPriority() > priority;
            case RIGHT:
                return (right.getPriority() > priority) || (right.getPriority() == priority
                        && priority > 0 && (!this.isDistributive() ||
                        (!right.isDistributive() && right.getPriority() < 4)));
            default:
                throw new UnsupportedOperationException("This operation is not supported");
        }
    }

    public String toMiniString() {
        boolean leftBraces = miniHelper(Type.LEFT);
        boolean rightBraces = miniHelper(Type.RIGHT);
        return (leftBraces ? "(" : "") + left.toMiniString() + (leftBraces ? ")" : "")
                + " " + this.getType() + " "
                + (rightBraces ? "(" : "") + right.toMiniString() + (rightBraces ? ")" : "");
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, this.getType());
    }

    protected abstract String getType();

    protected abstract int operate(int left, int right);

    //  protected abstract BigDecimal operate(BigDecimal left, BigDecimal right);

}
