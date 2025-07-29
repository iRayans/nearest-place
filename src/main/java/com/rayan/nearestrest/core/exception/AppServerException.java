package com.rayan.nearestrest.core.exception;

/**
 * Thrown when server-side errors occur
 */
public class AppServerException extends GenericException {

    private static final String DEFAULT_CODE = "SERVER_ERROR";

    public AppServerException(String message) {
        super(DEFAULT_CODE, message);
    }

    public AppServerException(String code, String message) {
        super(code, message);
    }

    public AppServerException(String message, Throwable cause) {
        super(DEFAULT_CODE, message);
        initCause(cause);
    }
}