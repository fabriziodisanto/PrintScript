package parser.statementsParser.expressionsParser.types;

import errors.ParserError;
import statement.expression.Expression;
import statement.expression.factory.GroupExpressionFactory;
import statement.expression.types.TokenExpression;
import parser.statementsParser.expressionsParser.ExpressionParserForm;
import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.List;

import static token.TokenType.*;

public class GroupingParser extends AbstractExpressionParser {

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
        int firstLeftParenIndex = super.getFirstEqualIndex(tokenList, LEFT_PAREN);
        int lastRightParenIndex = getLastRightParenIndex(tokenList);
        return tokenList.subList(firstLeftParenIndex, lastRightParenIndex);
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
