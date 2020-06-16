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
        Token type =  getTokenAfterThisTokenType(TokenType.COLON, tokens);
        List<Token> value = tokens.subList(5, tokens.size());
        if(value.isEmpty()) return new VariableStatement(tokens.get(0), name, type, null);
        return new VariableStatement(tokens.get(0), name, type, expressionParser.parse(value));
    }

    @Override
    public boolean matchThisTokens(List<Token> tokens) {
        if (tokens.size() < 6) return false;
        return (tokens.get(0).getType() == TokenType.LET
                && tokens.get(1).getType() == TokenType.IDENTIFIER
                && tokens.get(2).getType() == TokenType.COLON
                && (tokens.get(3).getType() == TokenType.STRING
                || tokens.get(3).getType() == TokenType.NUMBER
                || tokens.get(3).getType() == TokenType.BOOLEAN)
                && (tokens.get(tokens.size() - 1).getType() == TokenType.SEMICOLON
                || tokens.get(tokens.size() - 2).getType() == TokenType.SEMICOLON
                && tokens.get(tokens.size() - 1).getType() == TokenType.EOF));
    }
}
