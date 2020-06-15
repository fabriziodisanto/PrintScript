package expressions.helper;

import token.Token;

import java.util.List;

public class TokenExpression {
    private List<Token> left;
    private Token operator;
    private List<Token> right;

    public TokenExpression(List<Token> left, Token operator, List<Token> right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public List<Token> getLeft() {
        return left;
    }

    public Token getOperator() {
        return operator;
    }

    public List<Token> getRight() {
        return right;
    }
}
