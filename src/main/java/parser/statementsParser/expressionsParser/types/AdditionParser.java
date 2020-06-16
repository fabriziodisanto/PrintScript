package parser.statementsParser.expressionsParser.types;

import errors.ParserError;
import statement.expression.Expression;
import statement.expression.factory.BinaryExpressionFactory;
import statement.expression.types.TokenExpression;
import parser.statementsParser.expressionsParser.ExpressionParserForm;
import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.List;

import static token.TokenType.*;

public class AdditionParser extends AbstractExpressionParser {

    public AdditionParser() {
        super(ExpressionParserForm.LEFT_OPERATOR_RIGHT);
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
    public TokenExpression parse(List<Token> tokenList) throws ParserError {
        return parseLeftOpRight(tokenList);
    }
}
