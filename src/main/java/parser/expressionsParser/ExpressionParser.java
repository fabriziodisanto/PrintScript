package parser.expressionsParser;

import errors.ParserError;
import expressions.Expression;
import expressions.types.TokenExpression;
import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ExpressionParser {

    private List<TokenType> tokensToMatch;
    private ExpressionParserForm expressionParserForm;

    public ExpressionParser(ExpressionParserForm expressionParserForm) {
        this.tokensToMatch = getTokensToMatch();
        this.expressionParserForm = expressionParserForm;
    }

    public abstract TokenExpression parse(List<Token> tokens) throws ParserError;

    public TokenExpression parseLeftOpRight(List<Token> tokenList) throws ParserError {
        List<Token> left = new ArrayList<>();
        int i = 0;
        Token token = tokenList.get(i);
        try {
            while (!tokensToMatch.contains(token.getType())) {
                left.add(token);
                token = tokenList.get(++i);
            }
        } catch (IndexOutOfBoundsException exc) {
            throw new ParserError(token.getLineNumber(), token.getColPositionStart(), token.getColPositionEnd(), getStringsTokenToMatch());
        }
        Token operator = token;
        List<Token> right = tokenList.subList(++i, tokenList.size());
        if(left.isEmpty() || right.isEmpty())
            throw new ParserError("Invalid expression in line " + token.getLineNumber());
        return new TokenExpression(left, operator, right);
    }

    public String getStringsTokenToMatch(){
        String result = "[ " + tokensToMatch.get(0).toString();
        for (int i = 1; i < tokensToMatch.size(); i++) {
            result = result.concat(", ").concat(tokensToMatch.get(i).toString());
        }
        return result.concat(" ]");
    }

    TokenExpression parseOpRight(List<Token> tokenList){
        Token operator = tokenList.get(0);
        List<Token> right = tokenList.subList(1, tokenList.size());
        return new TokenExpression(null, operator, right);
    }

    public abstract List<TokenType> getTokensToMatch();

    public abstract Expression build(Expression left, Token operator, Expression right);

    public ExpressionParserForm getExpressionParserForm() {
        return expressionParserForm;
    }

    public ArrayList<TokenType> addAll(ArrayList<TokenType> tokenTypeList, TokenType ... types){
        tokenTypeList.addAll(Arrays.asList(types));
        return tokenTypeList;
    }
}
