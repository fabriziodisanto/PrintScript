package errors;

import token.TokenType;

public class InterpreterError extends Exception {
    public InterpreterError(String message, TokenType tokenType, int lineNumber, int colPositionStart, int colPositionEnd) {
        super(message + tokenType.toString() + " in line " + lineNumber + " column from " + colPositionStart + " to " + colPositionEnd);
    }

    public InterpreterError(String message) {
        super(message);
    }
}
