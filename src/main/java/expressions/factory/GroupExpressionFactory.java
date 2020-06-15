package expressions.factory;

import expressions.Expression;
import expressions.types.GroupExpression;

public class GroupExpressionFactory {
    static public GroupExpression buildGroupExpression(Expression expression) {
        return new GroupExpression(expression);
    }
}
