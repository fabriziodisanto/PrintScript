package parser.expressionsParser.types;

import errors.ParserError;
import expressions.Expression;
import expressions.factory.UnaryExpressionFactory;
import expressions.types.TokenExpression;
import parser.expressionsParser.ExpressionParser;
import parser.expressionsParser.ExpressionParserForm;
import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.List;

import static token.TokenType.*;

public class UnaryParser extends ExpressionParser {

    public UnaryParser() {
        super(ExpressionParserForm.OPERATOR_RIGHT);
    }

    @Override
    public List<TokenType> getTokensToMatch() {
        return addAll(new ArrayList<>(), MINUS);
    }

    @Override
    public TokenExpression parse(List<Token> tokenList) throws ParserError {
        return parseLeftOpRight(tokenList);
    }

    @Override
    public Expression build(Expression left, Token operator, Expression right) {
        return UnaryExpressionFactory.buildUnaryExpression(operator, right);
    }
}
