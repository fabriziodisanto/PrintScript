package parser;

import errors.ParserError;
import expressions.Expression;
import expressions.factory.ExpressionFactory;
import parser.expressionsParser.ExpressionParser;
import token.Token;

import java.util.List;
import java.util.stream.Stream;

public class ParserImpl implements Parser {

    private ExpressionFactory expressionFactory;
    //    private StatementFactory statementFactory;
    private int currentPosition = 0;
    private Stream<Token> tokenStream;
    private List<ExpressionParser> expressionParsers;

    public ParserImpl(ExpressionFactory expressionFactory, Stream<Token> tokenStream, List<ExpressionParser> expressionParsers) {
        this.expressionFactory = expressionFactory;
        this.tokenStream = tokenStream;
        this.expressionParsers = expressionParsers;
    }

    public Expression parse() {
        try {
            return
        } catch (ParserError error) {
            return null;
        }
    }



}
