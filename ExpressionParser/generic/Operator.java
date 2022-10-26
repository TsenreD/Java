package expression.generic;


public interface Operator<T> {
    T getConst(Number num);
    T Add (T left, T right);
    T Subtract (T left, T right);
    T Divide (T left, T right);
    T Multiply (T left, T right);
    T Min (T left, T right);
    T Max (T left, T right);
    T Count (T num);
    T Negate (T num);

}
