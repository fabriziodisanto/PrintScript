package parser.statementsParser;

import errors.ParserError;
import parser.statementsParser.expressionsParser.ExpressionParser;
import statement.Statement;
import statement.printStatement.PrintStatement;
import token.Token;
import token.TokenType;

import java.util.List;

public class PrintParser extends StatementParser{

    private ExpressionParser expressionParser;

    public PrintParser(ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;
    }

    @Override
    public Statement parse(List<Token> tokens) throws ParserError {
        return new PrintStatement(expressionParser
                .parse(getTokensBetweenThisTwo(tokens,TokenType.LEFT_PAREN, TokenType.RIGHT_PAREN)));
    }

    @Override
    public boolean matchThisTokens(List<Token> tokens) {
        if (tokens.size() < 5) return false;
        return tokens.get(0).getType() == TokenType.PRINT
                && tokens.get(1).getType() == TokenType.LEFT_PAREN
                && (tokens.get(tokens.size() - 2).getType() == TokenType.RIGHT_PAREN
                && tokens.get(tokens.size() - 1).getType() == TokenType.SEMICOLON)
                || (tokens.get(tokens.size() - 3).getType() == TokenType.RIGHT_PAREN
                && tokens.get(tokens.size() - 2).getType() == TokenType.SEMICOLON
                && tokens.get(tokens.size() - 1).getType() == TokenType.EOF);
    }

}
