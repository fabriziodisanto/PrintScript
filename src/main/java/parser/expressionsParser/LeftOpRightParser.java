package parser.expressionsParser;

import expressions.Expression;
import expressions.factory.ExpressionFactory;
import token.Token;

import java.util.stream.Stream;

abstract class LeftOpRightParser extends ExpressionParser{

    LeftOpRightParser(Stream<Token> tokenStream, ExpressionFactory expressionFactory, Expression nextExpression) {
        super(tokenStream, expressionFactory, nextExpression);
    }

//    public Expression parseSchema(){
//        Token operator = previous();
//        Expression right = super.nextExpression.parser();
//        expression = expressionFactory.buildBinaryExpression(expression, operator, right);
//    }
}
