package parser.expressionsParser;

import errors.ParserError;
import expressions.Expression;
import expressions.factory.ExpressionFactory;
import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static token.TokenType.*;

public class MultiplicationParser extends LeftOpRightParser{

    public MultiplicationParser(Stream<Token> tokenStream, ExpressionFactory expressionFactory, Expression nextExpression) {
        super(tokenStream, expressionFactory, nextExpression);
    }

    @Override
    List<TokenType> getTokensToMatch() {
        ArrayList<TokenType> tokensToMatch = new ArrayList<>();
        tokensToMatch.add(SLASH);
        tokensToMatch.add(STAR);
        return tokensToMatch;
    }

    @Override
    Expression parse() throws ParserError {
        Expression expression = unary();

        while (match(tokensToMatch)) {
            Token operator = previous();
            Expression right = unary();
            expression = expressionFactory.buildBinaryExpression(expression, operator, right);
        }

        return expression;
    }
}
