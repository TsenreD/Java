package expression.generic;

import expression.MainExpression;
import expression.exceptions.ExpressionParser;
import expression.exceptions.ParsingException;

public class Main {
    public static void main(String[] args) throws ParsingException {
        switch (args[0]) {
            case "-i" -> mainImpl(args[1], new CheckedIntegerOperator());

            case "-d" -> mainImpl(args[1], new DoubleOperator());

            case "-bi" -> mainImpl(args[1], new BigIntegerOperator());

            default -> throw new UnsupportedOperationException("This mode is not supported");
        }
        ;
    }

    private static <T> void mainImpl(String expression, Operator<T> operator) throws ParsingException {
        ExpressionParser<T> parser = new ExpressionParser<>();
        MainExpression<T> result = parser.parse(expression);
        StringBuilder ans = new StringBuilder();
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                for (int k = -2; k <= 2; k++) {
                    ans.append(result.SmartEval(operator.getConst(i), operator.getConst(j),
                            operator.getConst(k), operator)).append(" ");
                }
                ans.append(System.lineSeparator());
            }
        }
        System.out.println(ans);
    }
}
