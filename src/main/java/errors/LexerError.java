package errors;

public class LexerError extends Exception {

    public LexerError(int lineNumber, int colPositionStart, int currentPosition) {
        super("Error in line " + lineNumber + " column from " + colPositionStart + " to " + currentPosition);
    }

    public LexerError(String filename, int lineNumber, int colPositionStart, int currentPosition) {
        super("Error in file " + filename + " in line "+ lineNumber + " column from " + colPositionStart + " to " + currentPosition);
    }
}
