package expression.exceptions;

import expression.TripleExpression;

public class Main {
    public static void main(String[] args) {
        String test = " sapo + 5";
       try {
           TripleExpression e = new ExpressionParser().parse(test);
           System.out.println(e.evaluate(5, 6, 8));
       } catch (ParsingException e) {
           System.out.println(e.getMessage());
       }
    }
}