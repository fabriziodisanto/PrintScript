package parser.expressionsParser;

import errors.ParserError;
import expressions.Expression;
import expressions.factory.BinaryExpressionFactory;
import expressions.helper.TokenExpression;
import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.List;

import static token.TokenType.*;

public class MultiplicationParser extends ExpressionParser{

    public MultiplicationParser() {
        super(ExpressionType.LEFT_OPERATOR_RIGHT);
    }

    @Override
    public List<TokenType> getTokensToMatch() {
        return addAll(new ArrayList<>(), SLASH, STAR);
    }

    @Override
    public TokenExpression parse(List<Token> tokenList) throws ParserError {
        return parseLeftOpRight(tokenList);
    }

    @Override
    public Expression build(Expression left, Token operator, Expression right) {
        return BinaryExpressionFactory.buildBinaryExpression(left, operator, right);
    }
}
