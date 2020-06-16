package parser;

import errors.ParserError;
import parser.statementsParser.StatementParser;
import statement.Statement;
import token.Token;
import token.TokenType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static token.TokenType.EOF;

public class ParserImpl implements Parser {

    private int currentPosition;
    private Stream<Token> tokenStream;
    private Map<Integer, StatementParser> statementParserMap;
    private boolean lastExpression = false;
    private Stream<Statement> statements;

    public ParserImpl(Stream<Token> tokenStream, Map<Integer, StatementParser> statementParserMap) {
        this.tokenStream = tokenStream;
        this.statementParserMap = statementParserMap;
        this.statements = new ArrayList<Statement>().stream();
    }

//    split by semicolons?
    public Stream<Statement> analyze() throws ParserError {
        while (!lastExpression) {
            List<Token> tokenList = tokenStream.collect(Collectors.toList());
            List<Token> tokensUntilSemiColon = getTokensUntilThisType(tokenList, TokenType.SEMICOLON);
            tokenStream = tokenList.stream();
            addStatementToStream(parse(tokensUntilSemiColon));
        }
        return statements;
    }

    private void addStatementToStream(Statement statement) {
        statements = Stream.concat(statements, Stream.of(statement));
    }

    // TODO y con el if else que no terminan en ;
    // TODO unary and grouping statements.expressions
    public Statement parse(List<Token> tokens) throws ParserError {
         StatementParser statementParser = matchTokenWithStatementParser(tokens);
         if(statementParser == null) throw new ParserError("Statement cannot be parsed on line " + tokens.get(tokens.size()-1).getLineNumber());
         return statementParser.parse(tokens);
    }

    private StatementParser matchTokenWithStatementParser(List<Token> tokens) {
        for (Map.Entry<Integer, StatementParser> entry : statementParserMap.entrySet()) {
            if (entry.getValue().matchThisTokens(tokens)) return entry.getValue();
        }
        return null;
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
