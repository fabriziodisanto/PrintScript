package statement.expression.factory;

import statement.expression.types.VariableExpression;
import token.Token;

public class VariableExpressionFactory {

    static public VariableExpression buildVariableExpression(Token name) {
        return new VariableExpression(name);
    }
}
