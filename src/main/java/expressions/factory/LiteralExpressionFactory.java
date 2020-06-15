package expressions.factory;

import data.values.DataTypeValue;
import expressions.types.LiteralExpression;

public class LiteralExpressionFactory {

    static public LiteralExpression buildLiteralExpression(DataTypeValue value) {
        return new LiteralExpression(value);
    }

}
