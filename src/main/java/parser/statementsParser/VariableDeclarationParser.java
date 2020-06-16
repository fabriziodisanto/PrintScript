package parser.statementsParser;

import errors.ParserError;
import parser.statementsParser.expressionsParser.ExpressionParser;
import statement.Statement;
import statement.variableStatement.VariableStatement;
import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class VariableDeclarationParser extends StatementParser{

    private ExpressionParser expressionParser;
    private List<TokenType> validTypes;

    public VariableDeclarationParser(ExpressionParser expressionParser, boolean hasBooleans) {
        this.expressionParser = expressionParser;
        this.validTypes = getValidTypes(hasBooleans);
    }

    private List<TokenType> getValidTypes(boolean hasBooleans) {
        ArrayList<TokenType> tokenTypes = new ArrayList<>();
        addAll(tokenTypes, TokenType.STRING_VAR, TokenType.NUMBER_VAR);
        if(hasBooleans) tokenTypes.add(TokenType.BOOLEAN);
        return tokenTypes;
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
        if (tokens.size() < 5) return false;
        return ((tokens.get(0).getType() == TokenType.LET
                || tokens.get(0).getType() == TokenType.CONST)
                && tokens.get(1).getType() == TokenType.IDENTIFIER
                && tokens.get(2).getType() == TokenType.COLON
                && matchAnyOfThisTokensTypes(this.validTypes, tokens.get(3).getType())
                && (tokens.get(tokens.size() - 1).getType() == TokenType.SEMICOLON
                || (tokens.get(tokens.size() - 2).getType() == TokenType.SEMICOLON
                && tokens.get(tokens.size() - 1).getType() == TokenType.EOF)));

    }

    private boolean matchAnyOfThisTokensTypes(List<TokenType> validTypes, TokenType type) {
        return validTypes.contains(type);
    }
}
