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

public class AdditionParser extends LeftOpRightParser{

    public AdditionParser(Stream<Token> tokenStream, ExpressionFactory expressionFactory, Expression nextExpression) {
        super(tokenStream, expressionFactory, nextExpression);
    }

    @Override
    List<TokenType> getTokensToMatch() {
        ArrayList<TokenType> tokensToMatch = new ArrayList<>();
        tokensToMatch.add(MINUS);
        tokensToMatch.add(PLUS);
        return tokensToMatch;
    }

    @Override
    Expression parse() throws ParserError {
        Expression expression = multiplication();

        while (match(tokensToMatch)) {
            Token operator = previous();
            Expression right = multiplication();
            expression = expressionFactory.buildBinaryExpression(expression, operator, right);
        }

        return expression;
    }
}
