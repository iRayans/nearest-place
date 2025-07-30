package com.rayan.nearestrest.core.exception;

/**
 * Thrown when an entity cannot be found by its identifier
 */
public class ResultNotFoundException extends GenericException {

    private static final String DEFAULT_CODE = "NO_RESULTS_FOUND";


    public ResultNotFoundException(String message) {
        super(DEFAULT_CODE, message);
    }


    public ResultNotFoundException(String code, String message) {
        super(code, message);
    }

}