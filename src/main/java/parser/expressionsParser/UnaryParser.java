package parser.expressionsParser;

import errors.ParserError;
import expressions.Expression;
import expressions.factory.ExpressionFactory;
import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static token.TokenType.MINUS;
import static token.TokenType.PLUS;

public class UnaryParser extends OpRightParser {

    public UnaryParser(Stream<Token> tokenStream, ExpressionFactory expressionFactory, Expression nextExpression) {
        super(tokenStream, expressionFactory, nextExpression);
    }

    @Override
    List<TokenType> getTokensToMatch() {
        ArrayList<TokenType> tokensToMatch = new ArrayList<>();
        tokensToMatch.add(MINUS);
        return tokensToMatch;
    }

    @Override
    Expression parse() throws ParserError {
        if (match(tokensToMatch)) {
            Token operator = previous();
            Expression right = unary();
            return expressionFactory.buildUnaryExpression(operator, right);
        }

        return primary();
    }
}
