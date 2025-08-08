package com.rayan.nearestrest.core.exception;

/**
 * Thrown when invalid arguments are provided
 */
public class EntityInvalidArgumentsException extends GenericException {

    private static final String DEFAULT_CODE = "INVALID_ARGUMENTS";

    public EntityInvalidArgumentsException(String message) {
        super(DEFAULT_CODE, message);
    }

    public EntityInvalidArgumentsException(String code, String message) {
        super(code, message);
    }
}