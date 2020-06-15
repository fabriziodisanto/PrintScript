package parser;

import errors.ParserError;
import expressions.Expression;
import expressions.helper.TokenExpression;
import parser.expressionsParser.ExpressionParser;
import token.Token;
import token.TokenType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static token.TokenType.EOF;
import static token.TokenType.SEMICOLON;

public class ParserImpl implements Parser {

    private int currentPosition;
    private Stream<Token> tokenStream;
    private Map<Integer, ExpressionParser> expressionParsers;
    private boolean lastExpression = false;
    private Stream<Expression> expressions;

    public ParserImpl(Stream<Token> tokenStream, Map<Integer, ExpressionParser> expressionParsers) {
        this.tokenStream = tokenStream;
        this.expressionParsers = expressionParsers;
        this.expressions = new ArrayList<Expression>().stream();
    }

    public Stream<Expression> analyze() throws ParserError {
        while (!lastExpression) {
            List<Token> tokenList = tokenStream.collect(Collectors.toList());
            List<Token> tokensUntilSemiColon = getTokensUntilThisType(tokenList, TokenType.SEMICOLON);
            tokenStream = tokenList.stream();

            addExpressionToStream(parse(tokensUntilSemiColon));
            checkAllTokensBeenParsered(tokensUntilSemiColon);
        }
        return expressions;
    }

    private void checkAllTokensBeenParsered(List<Token> tokensUntilSemiColon) throws ParserError {
        for (Token token : tokensUntilSemiColon) {
            if(!token.hasBeenParsed() && token.getType()!= SEMICOLON) throw new ParserError(token.getLineNumber(), token.getColPositionStart(), token.getColPositionEnd());
        }
    }

    private void addExpressionToStream(Expression expression) {
        expressions = Stream.concat(expressions, Stream.of(expression));
    }

    //        TODO y con el if else que no terminan en ;
    public Expression parse(List<Token> tokens) throws ParserError {
        tokens = filterParseredTokens(tokens);
        ExpressionParser mostPrecedentParser = getMostPrecedentParser(tokens);
        TokenExpression tokenExpression = mostPrecedentParser.parse(tokens);
        Expression left = null;
        Token operator = null;
        Expression right = null;
        switch (mostPrecedentParser.getExpressionType()){
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
//        operator.setBeenParsed();
        return mostPrecedentParser.build(left, operator, right);
    }

    private List<Token> filterParseredTokens(List<Token> tokens) {
        List<Token> filtered = new ArrayList<>();
        for (Token token : tokens) {
            if(!token.hasBeenParsed()) filtered.add(token);
        }
        return filtered;
    }

    private ExpressionParser getMostPrecedentParser(List<Token> tokens) {
        HashMap<Integer, ExpressionParser> possibleParsers = new HashMap<>();
        for (Map.Entry<Integer,ExpressionParser> entry : expressionParsers.entrySet()){
            if(containsAny(entry.getValue().getTokensToMatch(), tokens))
                possibleParsers.put(entry.getKey(), entry.getValue());
        }
        ExpressionParser expressionParser = null;
        int i = 0;
        while (expressionParser == null) {
            expressionParser = possibleParsers.get(i);
            i++;
        }
        return expressionParser;
    }

    private boolean containsAny(List<TokenType> tokensToMatch, List<Token> tokensUntilSemiColon) {
        for (Token token : tokensUntilSemiColon) {
            if (tokensToMatch.contains(token.getType())) return true;
        }
        return false;
    }


    private ArrayList<Token> getTokensUntilThisType(List<Token> tokenList, TokenType tokenType) throws ParserError {
        ArrayList<Token> tokenArrayList = new ArrayList<>();
        int i = currentPosition;
        boolean found = false;
        Token token = null;
        for (; i < tokenList.size(); i++) {
            token = tokenList.get(i);
            tokenArrayList.add(token);
            if (token.getType() == tokenType) {
                found = true;
                break;
            }
        }
        currentPosition = i+1;
        if(i == tokenList.size() && !found) throw new ParserError("Expected Token " + tokenType.toString() + " not found in line " + token.getLineNumber());
        try {
            if (tokenList.get(++i).getType() == EOF) lastExpression = true;
        } catch (IndexOutOfBoundsException e) {}
        return tokenArrayList;
    }
}
