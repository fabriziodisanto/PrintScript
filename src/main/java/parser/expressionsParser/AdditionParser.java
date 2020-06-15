package parser.expressionsParser;

import expressions.Expression;
import expressions.factory.BinaryExpressionFactory;
import expressions.helper.TokenExpression;
import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.List;

import static token.TokenType.*;

public class AdditionParser extends ExpressionParser{

    public AdditionParser() {
        super(ExpressionType.LEFT_OPERATOR_RIGHT);
    }

    @Override
    public List<TokenType> getTokensToMatch() {
        ArrayList<TokenType> tokensToMatch = new ArrayList<>();
        tokensToMatch.add(MINUS);
        tokensToMatch.add(PLUS);
        return tokensToMatch;
    }

    @Override
    public Expression build(Expression left, Token operator, Expression right) {
        return BinaryExpressionFactory.buildBinaryExpression(left, operator, right);
    }

    @Override
    public TokenExpression parse(List<Token> tokenList){
        return parseLeftOpRight(tokenList);
    }
}
