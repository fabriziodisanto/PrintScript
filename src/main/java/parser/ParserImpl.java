package parser;

import errors.ParserError;
import expressions.Expression;
import expressions.helper.TokenExpression;
import parser.expressionsParser.ExpressionParser;
import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static token.TokenType.EOF;

public class ParserImpl implements Parser {

    //private StatementFactory statementFactory;
    private int currentPosition = 0;
    private Stream<Token> tokenStream;
    private Map<Integer, ExpressionParser> expressionParsers;
    private boolean lastExpression = false;
    private Stream<Expression> expressions;

    public ParserImpl(Stream<Token> tokenStream, Map<Integer, ExpressionParser> expressionParsers) {
        this.tokenStream = tokenStream;
        this.expressionParsers = expressionParsers;
        this.expressions = new ArrayList<Expression>().stream();
    }

    public Stream<Expression> analyze() {
        while (!lastExpression) {
            List<Token> tokensUntilSemiColon = getTokensUntilThisType(TokenType.SEMICOLON);
            try {
                addExpressionToStream(parse(tokensUntilSemiColon));
            }
            catch (ParserError parserError) {
                System.out.println(parserError.getMessage());
                System.exit(1);
            }
        }
        return expressions;
    }

    private void addExpressionToStream(Expression expression) {
        expressions = Stream.concat(expressions, Stream.of(expression));
    }

    //        TODO y con el if else que no terminan en ;
    public Expression parse(List<Token> tokens) throws ParserError {
        ExpressionParser mostPrecedentParser = getMostPrecedentParser(tokens);
        TokenExpression tokenExpression = mostPrecedentParser.parse(tokens);
        Expression left = null;
        Token operator = null;
        Expression right = null;
        switch (mostPrecedentParser.getExpressionType()){
            case LEFT_OPERATOR_RIGHT:
                left = parse(tokenExpression.getLeft());
                operator = tokenExpression.getOperator();
                right = parse(tokenExpression.getRight());
                break;
            case OPERATOR_RIGHT:
                operator = tokenExpression.getOperator();
                right = parse(tokenExpression.getRight());
                break;
            case OPERATOR:
                operator = tokenExpression.getOperator();
                break;
            case RIGHT:
                return parse(tokenExpression.getRight());
        }
        return mostPrecedentParser.build(left, operator, right);
    }

    private ExpressionParser getMostPrecedentParser(List<Token> tokensUntilSemiColon) {
        HashMap<Integer, ExpressionParser> possibleParsers = new HashMap<>();
        for (Map.Entry<Integer,ExpressionParser> entry : expressionParsers.entrySet()){
            if(containsAny(entry.getValue().getTokensToMatch(), tokensUntilSemiColon))
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


    private ArrayList<Token> getTokensUntilThisType(TokenType tokenType) {
        ArrayList<Token> tokenArrayList = new ArrayList<>();
        List<Token> tokenList = tokenStream.collect(Collectors.toList());
        for (Token token : tokenList) {
            currentPosition++;
            tokenArrayList.add(token);
            if(token.getType() == EOF) lastExpression = true;
            if(token.getType() == tokenType){
                break;
            }

        }
        tokenStream = tokenList.stream();
        return tokenArrayList;
    }

 /*   private TokenType getNextTokenType() {
        List<Token> tokenList = tokenStream.collect(Collectors.toList());
        tokenStream = tokenList.stream();
        return tokenList.get(currentPosition).getType();
    }

    private Token getNextToken() {
        List<Token> tokenList = tokenStream.collect(Collectors.toList());
        tokenStream = tokenList.stream();
        return tokenList.get(currentPosition);
    }*/
}
