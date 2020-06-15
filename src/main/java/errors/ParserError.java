package errors;

public class ParserError extends Exception {

    public ParserError(int lineNumber, int colPositionStart, int currentPosition, String expected) {
        super("Expected " + expected + " not found in line " + lineNumber + " column from "
                + colPositionStart + " to " + currentPosition);
    }

    public ParserError(int lineNumber, int colPositionStart, int currentPosition) {
        super("Token not parsed found in line " + lineNumber + " column from "
                + colPositionStart + " to " + currentPosition);
    }

    public ParserError(String message){
        super(message);
    }

}
