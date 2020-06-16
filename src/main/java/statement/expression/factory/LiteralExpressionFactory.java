package statement.expression.factory;

import data.values.DataTypeValue;
import statement.expression.types.LiteralExpression;

public class LiteralExpressionFactory {

    static public LiteralExpression buildLiteralExpression(DataTypeValue value) {
        return new LiteralExpression(value);
    }

}
