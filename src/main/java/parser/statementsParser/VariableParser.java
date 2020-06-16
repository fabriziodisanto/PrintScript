package parser.statementsParser;

import errors.ParserError;
import parser.statementsParser.expressionsParser.ExpressionParser;
import statement.Statement;
import statement.variableStatement.VariableStatement;
import token.Token;
import token.TokenType;

import java.util.List;

public class VariableParser extends StatementParser{

    private ExpressionParser expressionParser;

    public VariableParser(ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;
    }

    @Override
    public Statement parse(List<Token> tokens) throws ParserError {
        Token name = getTokenAfterThisTokenType(tokens.get(0).getType(), tokens);
        Token identifier =  getTokenAfterThisTokenType(TokenType.COLON, tokens);
        return new VariableStatement(tokens.get(0), name, identifier, expressionParser.parse(tokens.subList(5, tokens.size())));
    }

    @Override
    public boolean matchThisTokens(List<Token> tokens) {
        if (tokens.size() < 5) return false;
        return (tokens.get(0).getType() == TokenType.LET
                && tokens.get(1).getType() == TokenType.IDENTIFIER
                && tokens.get(2).getType() == TokenType.COLON
                && (tokens.get(3).getType() == TokenType.STRING
                || tokens.get(3).getType() == TokenType.NUMBER
                || tokens.get(3).getType() == TokenType.BOOLEAN)
                && tokens.get(4).getType() == TokenType.EQUAL
                && (tokens.get(tokens.size() - 1).getType() == TokenType.SEMICOLON
                || tokens.get(tokens.size() - 2).getType() == TokenType.SEMICOLON
                && tokens.get(tokens.size() - 1).getType() == TokenType.EOF));
    }
}
