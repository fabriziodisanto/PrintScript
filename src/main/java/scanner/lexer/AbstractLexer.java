package scanner.lexer;

import errors.LexerError;
import token.Token;
import token.factory.TokenFactory;


public abstract class AbstractLexer implements Lexer {
    StringBuffer codeSource;
    int lineNumber;
    int startPosition;
    int currentPosition;
    int colPositionStart;
    int colPositionEnd;
    TokenFactory tokenFactory;
    LexerType lexerType;

    public void setCodeSource(StringBuffer codeSource) {
        this.codeSource = codeSource;
    }

    Boolean isEndOfSource() {
        return codeSource == null || currentPosition >= codeSource.length();
    }

    char consumeNextChar() {
        currentPosition++;
        colPositionEnd++;
        return codeSource.charAt(currentPosition - 1);
    }

    char checkFollowingToken(int position) {
        if (currentPosition + position >= codeSource.length()) return '\0';
        return codeSource.charAt(currentPosition + position);
    }

    boolean isDigit(char character) {
        return character >= '0' && character <= '9';
    }

    boolean isAlpha(char character) {
        return (character >= 'a' && character <= 'z') ||
                (character >= 'A' && character <= 'Z') ||
                character == '_';
    }

    boolean isAlphaNumeric(char character) {
        return isAlpha(character) || isDigit(character);
    }

    boolean checkIfNextTokenIsThis(char character) {
        if (isEndOfSource()) return false;
        if (codeSource.charAt(currentPosition) != character) return false;
        currentPosition++;
        colPositionEnd++;
        return true;
    }

    public abstract Token scanToken() throws LexerError;

    boolean removeNotUsedChars(char c){
        if(c == ' ' || c == '\r' || c == '\t') {
            colPositionStart++;
            startPosition++;
            return true;
        }
        if (c == '\n'){
            lineNumber++;
            startPosition++;
            colPositionStart = 0;
            colPositionEnd = 0;
            return true;
        }
        return false;
    }

    public void setAllPositions(int lineNumber, int currentPosition, int colPositionEnd) {
        this.lineNumber = lineNumber;
        this.currentPosition = currentPosition;
        this.startPosition = currentPosition;
        this.colPositionStart = colPositionEnd;
        this.colPositionEnd = colPositionEnd;
    }

    public int[] getAllPositions(){
        int[] positions = new int[3];
        positions[0] = this.lineNumber;
        positions[1] = this.currentPosition;
        positions[2] = this.colPositionEnd;
        return positions;
    }

    public LexerType getLexerType() {
        return lexerType;
    }
}
