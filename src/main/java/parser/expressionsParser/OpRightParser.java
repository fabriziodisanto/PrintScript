package parser.expressionsParser;

import expressions.Expression;
import expressions.factory.ExpressionFactory;
import token.Token;

import java.util.stream.Stream;

public abstract class OpRightParser extends ExpressionParser{

    OpRightParser(Stream<Token> tokenStream, ExpressionFactory expressionFactory, Expression nextExpression) {
        super(tokenStream, expressionFactory, nextExpression);
    }
//    public Expression parseSchema(){
//        Token operator = previous();
//        expression = expressionFactory.buildBinaryExpression(operator, expression);
//    }
}
