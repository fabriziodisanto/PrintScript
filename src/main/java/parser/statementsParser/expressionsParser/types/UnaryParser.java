package parser.statementsParser.expressionsParser.types;

import errors.ParserError;
import statement.expression.Expression;
import statement.expression.factory.UnaryExpressionFactory;
import statement.expression.types.TokenExpression;
import parser.statementsParser.expressionsParser.ExpressionParserForm;
import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.List;

import static token.TokenType.*;

public class UnaryParser extends AbstractExpressionParser {

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
