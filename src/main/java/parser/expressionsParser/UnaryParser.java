package parser.expressionsParser;

import expressions.Expression;
import expressions.factory.UnaryExpressionFactory;
import expressions.helper.TokenExpression;
import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.List;

import static token.TokenType.*;

public class UnaryParser extends ExpressionParser {

    public UnaryParser() {
        super(ExpressionType.OPERATOR_RIGHT);
    }

    @Override
    public List<TokenType> getTokensToMatch() {
        return addAll(new ArrayList<>(), MINUS);
    }

    @Override
    public TokenExpression parse(List<Token> tokenList){
        return parseLeftOpRight(tokenList);
    }


    @Override
    public Expression build(Expression left, Token operator, Expression right) {
        return UnaryExpressionFactory.buildUnaryExpression(operator, right);
    }
}
