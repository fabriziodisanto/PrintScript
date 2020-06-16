package parser.statementsParser;

import errors.ParserError;
import statement.Statement;

import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class StatementParser {


    public abstract Statement parse(List<Token> tokens) throws ParserError;

    public ArrayList<TokenType> addAll(ArrayList<TokenType> tokenTypeList, TokenType ... types){
        tokenTypeList.addAll(Arrays.asList(types));
        return tokenTypeList;
    }

    public abstract boolean matchThisTokens(List<Token> tokens);

    Token getTokenAfterThisTokenType(TokenType tokenType, List<Token> tokens) throws ParserError {
        int i = 0;
        for (; i < tokens.size(); i++) {
            if(tokens.get(i).getType() == tokenType) break;
        }
        if(i + 1>= tokens.size()) throw new ParserError("Invalid statement in line " + tokens.get(i-1).getLineNumber());
        return tokens.get(++i);
    }

    List<Token> getTokensBetweenThisTwo(List<Token> tokens, TokenType left, TokenType right) throws ParserError {
        int leftIndex = -1;
        int rightIndex = -1;
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).getType() == left && leftIndex < 0) leftIndex = i;
            if (tokens.get(i).getType() == right) rightIndex = i;
        }
        if(leftIndex < 0 || rightIndex < 0 || leftIndex > rightIndex) throw new ParserError("Invalid statement in line " + tokens.get(tokens.size()-1).getLineNumber());
        return tokens.subList(leftIndex + 1, rightIndex);
    }

    boolean containsAllTheseTokensTypes(List<Token> tokens, ArrayList<TokenType> tokenTypes) {
        for (TokenType type : tokenTypes) {
            if(!containsThisTokenType(tokens, type)) return false;
        }
        return true;
    }

    boolean containsThisTokenType(List<Token> tokens, TokenType type) {
        for (Token token : tokens) {
            if(token.getType() == type) return true;
        }
        return false;
    }
}
