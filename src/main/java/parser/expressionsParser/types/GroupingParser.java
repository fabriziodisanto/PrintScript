package parser.expressionsParser.types;

import errors.ParserError;
import expressions.Expression;
import expressions.factory.GroupExpressionFactory;
import expressions.types.TokenExpression;
import parser.expressionsParser.ExpressionParser;
import parser.expressionsParser.ExpressionParserForm;
import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.List;

import static token.TokenType.*;

public class GroupingParser extends ExpressionParser {

    public GroupingParser() {
        super(ExpressionParserForm.RIGHT);
    }

    @Override
    public List<TokenType> getTokensToMatch() {
        return addAll(new ArrayList<>(), LEFT_PAREN);
    }

    @Override
    public TokenExpression parse(List<Token> tokenList) throws ParserError {
        List<Token> tokens = getExpressionInsideParens(tokenList);
        return new TokenExpression(null, null, tokens);
    }

    private List<Token> getExpressionInsideParens(List<Token> tokenList) throws ParserError {
        int firstLeftParenIndex = getFirstLeftParenIndex(tokenList);
        int lastRightParenIndex = getLastRightParenIndex(tokenList);
        return tokenList.subList(firstLeftParenIndex, lastRightParenIndex);
    }

    private int getFirstLeftParenIndex(List<Token> tokenList) throws ParserError {
        int i = 0;
        Token lastToken = null;
        for (; i < tokenList.size(); i++) {
            lastToken = tokenList.get(i);
            if (lastToken.getType() == LEFT_PAREN) return i+1;
        }
        throw new ParserError(lastToken.getLineNumber(), lastToken.getColPositionStart(), lastToken.getColPositionEnd(), "(");
    }

    private int getLastRightParenIndex(List<Token> tokenList) throws ParserError {
        int index = 0;
        int i = 0;
        Token lastToken = null;
        for (; i < tokenList.size(); i++) {
            lastToken = tokenList.get(i);
            if (lastToken.getType() == RIGHT_PAREN) index = i;
        }
        if (index != 0) return index;
        throw new ParserError(lastToken.getLineNumber(), lastToken.getColPositionStart(), lastToken.getColPositionEnd(), ")");
    }

    @Override
    public Expression build(Expression left, Token operator, Expression right) {
        return GroupExpressionFactory.buildGroupExpression(right);
    }
}
