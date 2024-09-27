package org.eru.errorhandling;

public class BaseException extends Exception {
    public String ErrorCode;
    public String[] MessageVars;
    public int StatusCode;
    public int NumericErrorCode;
    public BaseException(String message) {
        super(message);
        ErrorCode = "org.eru.errors.BaseException";
        MessageVars = new String[0];
        StatusCode = 500;
        NumericErrorCode = -1;
    }

    public BaseException(String errorCode, String message, int statusCode) {
        super(message);
        ErrorCode = errorCode;
        MessageVars = new String[0];
        StatusCode = statusCode;
        NumericErrorCode = -1;
    }

    public BaseException(String errorCode, String message, int statusCode, int numericErrorCode) {
        super(message);
        ErrorCode = errorCode;
        MessageVars = new String[0];
        StatusCode = statusCode;
        NumericErrorCode = numericErrorCode;
    }

    public BaseException(String errorCode, String message, String[] messageVars, int statusCode, int numericErrorCode) {
        super(message);
        ErrorCode = errorCode;
        MessageVars = messageVars;
        StatusCode = statusCode;
        NumericErrorCode = numericErrorCode;
    }
}
