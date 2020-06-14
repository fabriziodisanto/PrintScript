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

public class ComparisonParser extends LeftOpRightParser{

    public ComparisonParser(Stream<Token> tokenStream, ExpressionFactory expressionFactory, Expression nextExpression) {
        super(tokenStream, expressionFactory, nextExpression);
    }

    @Override
    List<TokenType> getTokensToMatch() {
        ArrayList<TokenType> tokensToMatch = new ArrayList<>();
        tokensToMatch.add(GREATER);
        tokensToMatch.add(GREATER_EQUAL);
        tokensToMatch.add(LESS);
        tokensToMatch.add(LESS_EQUAL);
        return tokensToMatch;
    }

    @Override
    Expression parse() throws ParserError {
        Expression expression = addition();

        while (match(tokensToMatch)) {
            Token operator = previous();
            Expression right = addition();
            expression = expressionFactory.buildBinaryExpression(expression, operator, right);
        }

        return expression;
    }
}
