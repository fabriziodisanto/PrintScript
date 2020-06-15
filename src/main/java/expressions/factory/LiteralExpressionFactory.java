package expressions.factory;

import data.DataTypeValue;
import expressions.LiteralExpression;

public class LiteralExpressionFactory {

    static public LiteralExpression buildLiteralExpression(DataTypeValue value) {
        return new LiteralExpression(value);
    }

}
