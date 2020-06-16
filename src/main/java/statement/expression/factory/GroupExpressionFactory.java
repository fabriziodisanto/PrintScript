package statement.expression.factory;

import statement.expression.Expression;
import statement.expression.types.GroupExpression;

public class GroupExpressionFactory {
    static public GroupExpression buildGroupExpression(Expression expression) {
        return new GroupExpression(expression);
    }
}
