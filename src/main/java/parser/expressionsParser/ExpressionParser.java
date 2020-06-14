package parser.expressionsParser;

import errors.ParserError;
import expressions.Expression;
import expressions.factory.ExpressionFactory;
import token.Token;
import token.TokenType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static token.TokenType.*;
import static token.TokenType.RIGHT_PAREN;

public abstract class ExpressionParser {

    Expression nextExpression;
    List<TokenType> tokensToMatch;
    ExpressionFactory expressionFactory;
    int currentPosition = 0;
    Stream<Token> tokenStream;

    public ExpressionParser(Stream<Token> tokenStream, ExpressionFactory expressionFactory, Expression nextExpression) {
        this.tokensToMatch = getTokensToMatch();
    }

    abstract Expression parse() throws ParserError;

    abstract List<TokenType> getTokensToMatch();

    boolean match(List<TokenType> types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    boolean match(TokenType type) {
        if (check(type)) {
            advance();
            return true;
        }
        return false;
    }

    boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    Token advance() {
        if (!isAtEnd()) currentPosition++;
        return previous();
    }

    boolean isAtEnd() {
        return peek().getType() == EOF;
    }

    Token peek() {
        return getTokenOnThisPosition(currentPosition);
    }

    Token previous() {
        return getTokenOnThisPosition(currentPosition - 1);
    }

    Token getTokenOnThisPosition(int i) {
        List<Token> tokenList = tokenStream.collect(Collectors.toList());
        tokenStream = tokenList.stream();
        return tokenList.get(i);
    }


    //    todo hacerlo bien
//    Expression expression() throws ParserError {
////        return assigment();
//        return comparison();
//    }


//    private void synchronize() {
//        advance();
//
//        while (!isAtEnd()) {
//            if (previous().getType() == SEMICOLON) return;
//            switch (peek().getType()) {
//                case IF:
//                case PRINTLN:
//                case IMPORT:
//                    return;
//            }
//
//            advance();
//        }
//    }

}
