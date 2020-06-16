package parser.statementsParser.expressionsParser.types;

import errors.ParserError;
import statement.expression.Expression;
import statement.expression.factory.AssingExpressionFactory;
import statement.expression.types.TokenExpression;
import parser.statementsParser.expressionsParser.ExpressionParserForm;
import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.List;

import static token.TokenType.*;

public class AssignExpressionParser extends AbstractExpressionParser {

    public AssignExpressionParser() {
        super(ExpressionParserForm.OPERATOR_RIGHT);
    }

    @Override
    public List<TokenType> getTokensToMatch() {
        ArrayList<TokenType> tokensToMatch = new ArrayList<>();
        return addAll(tokensToMatch, EQUAL);
    }

    @Override
    public Expression build(Expression left, Token operator, Expression right) {
        return AssingExpressionFactory.buildAssignExpression(operator, right);
    }

    @Override
    public TokenExpression parse(List<Token> tokenList) throws ParserError {
        return parseOpRight(tokenList);
    }

    public TokenExpression parseOpRight(List<Token> tokenList) throws ParserError {
        int equalIndex = getFirstEqualIndex(tokenList, EQUAL);
        Token equal = tokenList.get(equalIndex);
        equal.setBeenParsed();
        List<Token> right = tokenList.subList(equalIndex+1, tokenList.size());
        return new TokenExpression(null, tokenList.get(0), right);
    }
}
