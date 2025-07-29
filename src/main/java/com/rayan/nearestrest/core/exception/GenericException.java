package com.rayan.nearestrest.core.exception;

/**
 * Base exception class for all application exceptions.
 * Extends RuntimeException to avoid forcing callers to catch exceptions
 * they can't meaningfully recover from.
 */
public abstract class GenericException extends RuntimeException {

    private final String code;

    /**
     * Creates a new exception with an error code and message
     *
     * @param code A string code identifying the error type
     * @param message A human-readable error message
     */
    public GenericException(String code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * @return The error code associated with this exception
     */
    public String getCode() {
        return code;
    }
}