package expressions.factory;

import expressions.types.VariableExpression;
import token.Token;

public class VariableExpressionFactory {

    static public VariableExpression buildVariableExpression(Token name) {
        return new VariableExpression(name);
    }
}
