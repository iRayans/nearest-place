package com.rayan.nearestrest.dto;

import java.time.LocalDateTime;

/**
 * Standard error response format for the API
 */
public class ErrorMessageDTO {

    private String code;
    private String message;
    private String path;
    private String timestamp;

    /**
     * Creates a new error message
     *
     * @param code The error code
     * @param message The error message
     * @param path The request path that caused the error
     */
    public ErrorMessageDTO(String code, String message, String path) {
        this.code = code;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now().toString();
    }

    // Getters and setters

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}