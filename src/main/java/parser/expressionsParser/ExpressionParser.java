package parser.expressionsParser;

import errors.ParserError;
import expressions.Expression;
import expressions.helper.TokenExpression;
import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ExpressionParser {

    private List<TokenType> tokensToMatch;
    private ExpressionType expressionType;

    public ExpressionParser(ExpressionType expressionType) {
        this.tokensToMatch = getTokensToMatch();
        this.expressionType = expressionType;
    }

    public abstract TokenExpression parse(List<Token> tokens) throws ParserError;

    TokenExpression parseLeftOpRight(List<Token> tokenList){
        List<Token> left = new ArrayList<>();
        int i = 0;
        Token token = tokenList.get(i);
        while(!tokensToMatch.contains(token)){
            left.add(token);
            token = tokenList.get(++i);
        }
        Token operator = token;
        List<Token> right = tokenList.subList(++i, tokenList.size());
        return new TokenExpression(left, operator, right);
    }

    TokenExpression parseOpRight(List<Token> tokenList){
        Token operator = tokenList.get(0);
        List<Token> right = tokenList.subList(1, tokenList.size());
        return new TokenExpression(null, operator, right);
    }

    public abstract List<TokenType> getTokensToMatch();

    public abstract Expression build(Expression left, Token operator, Expression right);

    public ExpressionType getExpressionType() {
        return expressionType;
    }

    ArrayList<TokenType> addAll(ArrayList<TokenType> tokenTypeList, TokenType ... types){
        tokenTypeList.addAll(Arrays.asList(types));
        return tokenTypeList;
    }
}
