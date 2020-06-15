package parser.expressionsParser;

import errors.ParserError;
import expressions.Expression;
import expressions.factory.LiteralExpressionFactory;
import expressions.helper.TokenExpression;
import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.List;

import static token.TokenType.*;

public class PrimaryParser extends ExpressionParser{

    public PrimaryParser() {
        super(ExpressionType.OPERATOR);
    }

    @Override
    public List<TokenType> getTokensToMatch() {
        return addAll(new ArrayList<>(), NUMBER, STRING, FALSE, TRUE);
    }

    @Override
    public TokenExpression parse(List<Token> tokenList) throws ParserError {
        return new TokenExpression(null, getFirstPrimary(tokenList), null);
    }

    private Token getFirstPrimary(List<Token> tokenList) throws ParserError {
        int i = 0;
        Token lastToken = null;
        for (; i < tokenList.size(); i++) {
            lastToken = tokenList.get(i);
            if (getTokensToMatch().contains(lastToken.getType())) return lastToken;
        }
        throw new ParserError(lastToken.getLineNumber(), lastToken.getColPositionStart(), lastToken.getColPositionEnd(), getStringsTokenToMatch());
    }


    //    todo mhmh
    @Override
    public Expression build(Expression left, Token operator, Expression right) {
        return LiteralExpressionFactory.buildLiteralExpression(operator.getValue());
    }
}
