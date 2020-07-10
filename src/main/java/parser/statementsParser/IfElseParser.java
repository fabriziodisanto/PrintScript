package parser.statementsParser;

import errors.ParserError;
import parser.Parser;
import parser.statementsParser.expressionsParser.ExpressionParser;
import statement.Statement;
import statement.ifStatement.IfElseStatement;
import token.Token;
import token.TokenType;

import java.util.List;

public class IfElseParser extends StatementParser {

    private ExpressionParser expressionParser;
    private Parser parser;

    public IfElseParser(ExpressionParser expressionParser, Parser parser) {
        this.expressionParser = expressionParser;
        this.parser = parser;
    }

    @Override
    public Statement parse(List<Token> tokens) throws ParserError {
        return new IfElseStatement(expressionParser.parse(getTokensBetweenThisTwo(tokens, TokenType.LEFT_PAREN, TokenType.RIGHT_PAREN)),
                parser.parse(getTokensBetweenThisTwo(tokens, TokenType.LEFT_BRACE, TokenType.RIGHT_BRACE)),
                parser.parse(getTokensBetweenThisTwo(tokens, TokenType.LEFT_BRACE, TokenType.RIGHT_BRACE)));
    }

    @Override
    public boolean matchThisTokens(List<Token> tokens) throws ParserError {
        if (tokens.size() < 5) return false;
        return tokens.get(0).getType() == TokenType.IF
                && super.getTokensBetweenThisTwo(tokens, TokenType.LEFT_PAREN, TokenType.RIGHT_PAREN).size() > 0
                && super.getTokensBetweenThisTwo(tokens, TokenType.LEFT_BRACE, TokenType.RIGHT_BRACE).size() > 0
                && super.containsThisTokenType(tokens, TokenType.ELSE)
                && (tokens.get(tokens.size() - 1).getType() == TokenType.SEMICOLON
                || (tokens.get(tokens.size() - 2).getType() == TokenType.SEMICOLON
                && tokens.get(tokens.size() - 1).getType() == TokenType.EOF));
    }
}