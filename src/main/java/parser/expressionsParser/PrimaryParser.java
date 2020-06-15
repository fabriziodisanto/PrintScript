package parser.expressionsParser;

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
    public TokenExpression parse(List<Token> tokenList){
        return new TokenExpression(null, tokenList.get(0), null);
    }


    //    todo mhmh
    @Override
    public Expression build(Expression left, Token operator, Expression right) {
        return LiteralExpressionFactory.buildLiteralExpression(operator.getValue());
    }
}
