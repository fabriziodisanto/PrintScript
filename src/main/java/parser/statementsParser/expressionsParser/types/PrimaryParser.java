package parser.statementsParser.expressionsParser.types;

import errors.ParserError;
import statement.expression.Expression;
import statement.expression.factory.LiteralExpressionFactory;
import statement.expression.types.TokenExpression;
import parser.statementsParser.expressionsParser.ExpressionParserForm;
import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.List;

import static token.TokenType.*;

public class PrimaryParser extends AbstractExpressionParser {

    public PrimaryParser() {
        super(ExpressionParserForm.OPERATOR);
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
