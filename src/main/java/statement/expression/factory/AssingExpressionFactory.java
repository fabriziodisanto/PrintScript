package statement.expression.factory;

import statement.expression.types.AssignExpression;
import statement.expression.Expression;
import token.Token;

public class AssingExpressionFactory {

    static public AssignExpression buildAssignExpression(Token name, Expression value) {
        return new AssignExpression(name, value);
    }
}
