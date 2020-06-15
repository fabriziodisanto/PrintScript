package expressions.factory;

import expressions.types.BinaryExpression;
import expressions.Expression;
import token.Token;

public class BinaryExpressionFactory {

    static public BinaryExpression buildBinaryExpression(Expression leftExpression, Token operator, Expression rightExpression) {
        return new BinaryExpression(leftExpression, operator, rightExpression);
    }

}
