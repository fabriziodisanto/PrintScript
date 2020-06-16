package statement.expression.factory;

import statement.expression.Expression;
import statement.expression.types.UnaryExpression;
import token.Token;

public class UnaryExpressionFactory {

    static public UnaryExpression buildUnaryExpression(Token operator, Expression rightExpression) {
        return new UnaryExpression(operator, rightExpression);
    }
}
