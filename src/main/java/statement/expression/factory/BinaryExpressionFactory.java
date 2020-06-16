package statement.expression.factory;

import statement.expression.types.BinaryExpression;
import statement.expression.Expression;
import token.Token;

public class BinaryExpressionFactory {

    static public BinaryExpression buildBinaryExpression(Expression leftExpression, Token operator, Expression rightExpression) {
        return new BinaryExpression(leftExpression, operator, rightExpression);
    }

}
