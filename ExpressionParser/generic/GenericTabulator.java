package expression.generic;

import expression.MainExpression;
import expression.exceptions.ExpressionException;
import expression.exceptions.ExpressionParser;
import expression.exceptions.ParsingException;

public class GenericTabulator implements Tabulator {
    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2,
                                 int y1, int y2, int z1, int z2) throws Exception {
        return switch (mode) {
            case "i" -> tabImpl(expression, x1, x2, y1, y2, z1, z2, new CheckedIntegerOperator());

            case "d" -> tabImpl(expression, x1, x2, y1, y2, z1, z2, new DoubleOperator());

            case "bi" -> tabImpl(expression, x1, x2, y1, y2, z1, z2, new BigIntegerOperator());

            case "u" -> tabImpl(expression, x1, x2, y1, y2, z1, z2, new IntegerOperator());

            case "l" -> tabImpl(expression, x1, x2, y1, y2, z1, z2, new LongOperator());

            case "t" -> tabImpl(expression, x1, x2, y1, y2, z1, z2, new TruncateOperator());

            default -> throw new UnsupportedOperationException("This mode is not supported");
        };
    }

    private <T> Object[][][] tabImpl(String expression, int x1, int x2, int y1, int y2, int z1, int z2,
                                     Operator<T> operator) throws ParsingException {
        ExpressionParser<T> parser = new ExpressionParser<>();
        Object[][][] ans = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        MainExpression<T> result = parser.parse(expression);
        for (int i = 0; i <= x2 - x1; i++) {
            for (int j = 0; j <= y2 - y1; j++) {
                for (int k = 0; k <= z2 - z1; k++) {
                    try {
                        ans[i][j][k] = result.SmartEval(operator.getConst(x1 + i), operator.getConst(y1 + j),
                                operator.getConst(z1 + k), operator);
                    } catch (ExpressionException | ArithmeticException e) {
                        ans[i][j][k] = null;
                    }
                }
            }
        }
        return ans;
    }
}
