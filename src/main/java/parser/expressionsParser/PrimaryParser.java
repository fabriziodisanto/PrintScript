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

public class PrimaryParser extends LeftOpRightParser{

    public PrimaryParser(Stream<Token> tokenStream, ExpressionFactory expressionFactory, Expression nextExpression) {
        super(tokenStream, expressionFactory, nextExpression);
    }

    @Override
    List<TokenType> getTokensToMatch() {
        ArrayList<TokenType> tokensToMatch = new ArrayList<>();
        tokensToMatch.add(NUMBER);
        tokensToMatch.add(STRING);
        tokensToMatch.add(FALSE);
        tokensToMatch.add(TRUE);
        return tokensToMatch;
    }

    @Override
    Expression parse() throws ParserError {
        if (match(tokensToMatch)) {
            return expressionFactory.buildLiteralExpression(previous().getValue());
        }
        if (match(LEFT_PAREN)) {
            Expression expression = expression();
            try {
                consume(RIGHT_PAREN);
            } catch (ParserError parserError) {
                System.out.println(parserError.getMessage());
//                todo mmhmh queremos exitear?
                System.exit(1);
            }
            return expressionFactory.buildGroupExpression(expression);
        }
        Token previousToken = peek();
        throw new ParserError(previousToken.getLineNumber(), previousToken.getColPositionStart(), previousToken.getColPositionEnd(), "expression");
    }

    Token consume(TokenType type) throws ParserError {
        if (check(type)) return advance();
        Token tokenError = peek();
        throw new ParserError(tokenError.getLineNumber(), tokenError.getColPositionStart(), tokenError.getColPositionEnd(), tokenError.getType().toString());
    }
}
