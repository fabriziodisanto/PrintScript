package errors;

import token.TokenType;

public class ParserError extends Exception {

    public ParserError(int lineNumber, int colPositionStart, int currentPosition, String expected) {
        super("Expected " + expected + " not found in line " + lineNumber + " column from "
                + colPositionStart + " to " + currentPosition);
    }
}
