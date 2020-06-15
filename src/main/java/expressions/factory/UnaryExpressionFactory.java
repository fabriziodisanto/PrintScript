package expressions.factory;

import expressions.Expression;
import expressions.UnaryExpression;
import token.Token;

public class UnaryExpressionFactory {

    static public UnaryExpression buildUnaryExpression(Token operator, Expression rightExpression) {
        return new UnaryExpression(operator, rightExpression);
    }
}
