package parser.statementsParser.expressionsParser;

import errors.ParserError;
import parser.statementsParser.StatementParser;
import parser.statementsParser.expressionsParser.types.AbstractExpressionParser;
import statement.expression.Expression;
import statement.expression.types.TokenExpression;
import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressionParser extends StatementParser {

    private Map<Integer, AbstractExpressionParser> expressionParsers;

    public ExpressionParser(Map<Integer, AbstractExpressionParser> expressionParsers) {
        this.expressionParsers = expressionParsers;
    }


//    todo probar con super.parse() ??
//    todo que no maneje mas si es el end of file;
    public Expression parse(List<Token> tokens) throws ParserError {
        tokens = filterParseredTokens(tokens);
        if(tokens.size() == 1 && tokens.get(0).getType() == TokenType.SEMICOLON) return null;
        AbstractExpressionParser mostPrecedentParser = getMostPrecedentParser(tokens);
        TokenExpression tokenExpression = mostPrecedentParser.parse(tokens);
        Expression left = null;
        Token operator = null;
        Expression right = null;
        switch (mostPrecedentParser.getExpressionParserForm()){
            case LEFT_OPERATOR_RIGHT:
                left = parse(tokenExpression.getLeft());
                operator = tokenExpression.getOperator();
                operator.setBeenParsed();
                right = parse(tokenExpression.getRight());
                break;
            case OPERATOR_RIGHT:
                operator = tokenExpression.getOperator();
                operator.setBeenParsed();
                right = parse(tokenExpression.getRight());
                break;
            case OPERATOR:
                operator = tokenExpression.getOperator();
                operator.setBeenParsed();
                break;
            case RIGHT:
                return parse(tokenExpression.getRight());
        }
        return mostPrecedentParser.build(left, operator, right);
    }

    @Override
    public boolean matchThisTokens(List<Token> tokens) {
        return true;
    }

    private List<Token> filterParseredTokens(List<Token> tokens) {
        List<Token> filtered = new ArrayList<>();
        for (Token token : tokens) {
            if(!token.hasBeenParsed()) filtered.add(token);
        }
        return filtered;
    }

    private AbstractExpressionParser getMostPrecedentParser(List<Token> tokens) throws ParserError {
        HashMap<Integer, AbstractExpressionParser> possibleParsers = new HashMap<>();
        for (Map.Entry<Integer, AbstractExpressionParser> entry : expressionParsers.entrySet()){
            if(containsAny(entry.getValue().getTokensToMatch(), tokens))
                possibleParsers.put(entry.getKey(), entry.getValue());
        }
        if (possibleParsers.isEmpty()) throw new ParserError("Expression could not be parsed in line " + tokens.get(tokens.size()-1).getLineNumber());
        AbstractExpressionParser abstractExpressionParser = null;
        int i = 0;
        while (abstractExpressionParser == null) {
            abstractExpressionParser = possibleParsers.get(i);
            i++;
        }
        return abstractExpressionParser;
    }

    private boolean containsAny(List<TokenType> tokensToMatch, List<Token> tokensUntilSemiColon) {
        for (Token token : tokensUntilSemiColon) {
            if (tokensToMatch.contains(token.getType())) return true;
        }
        return false;
    }

}
