package parser.statementsParser.expressionsParser.types;

import errors.ParserError;
import statement.expression.Expression;
import statement.expression.factory.LiteralExpressionFactory;
import statement.expression.factory.VariableExpressionFactory;
import statement.expression.types.TokenExpression;
import parser.statementsParser.expressionsParser.ExpressionParserForm;
import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.List;

import static token.TokenType.*;

public class VariableParser extends AbstractExpressionParser {

    public VariableParser() {
        super(ExpressionParserForm.OPERATOR);
    }

    @Override
    public List<TokenType> getTokensToMatch() {
        return addAll(new ArrayList<>(), IDENTIFIER);
    }

    @Override
    public TokenExpression parse(List<Token> tokenList) throws ParserError {
        return new TokenExpression(null, tokenList.get(0), null);
    }

    @Override
    public Expression build(Expression left, Token operator, Expression right) {
        return VariableExpressionFactory.buildVariableExpression(operator);
    }
}
