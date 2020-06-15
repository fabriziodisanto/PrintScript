package expressions.factory;

import expressions.AssignExpression;
import expressions.Expression;
import token.Token;

public class AssingExpressionFactory {

    static public AssignExpression buildAssignExpression(Token name, Expression value) {
        return new AssignExpression(name, value);
    }
}
