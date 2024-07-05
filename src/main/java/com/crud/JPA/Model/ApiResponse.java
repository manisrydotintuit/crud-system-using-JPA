package com.crud.JPA.Model;

import java.time.LocalDateTime;

public class ApiResponse<T> {

    private T data;
    private boolean success;
    private LocalDateTime timestamp;
    private int statusCode;
    private String message;
    private String errorMessage;

    // Default constructor
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor for success response
    public ApiResponse(T data, boolean success, int statusCode, String message, String errorMessage) {
        this.data = data;
        this.success = success;
        this.timestamp = LocalDateTime.now();
        this.statusCode = statusCode;
        this.message = message;
        this.errorMessage = errorMessage;
    }

    // Constructor for success response without data
    public ApiResponse(boolean success, int statusCode, String message, String errorMessage) {
        this.success = success;
        this.timestamp = LocalDateTime.now();
        this.statusCode = statusCode;
        this.message = message;
        this.errorMessage = errorMessage;
    }

    // Getters and setters
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
