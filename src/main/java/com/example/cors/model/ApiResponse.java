package com.example.cors.model;

/**
 * API Response Model
 * 
 * Response Flow:
 * ┌──────────────┐
 * │ ApiResponse  │
 * │ status + msg │
 * └──────────────┘
 *        │
 *        ▼
 * ┌──────────────┐
 * │  JSON Data   │
 * │ {           │
 * │  status: X, │
 * │  message: Y │
 * │ }           │
 * └──────────────┘
 */
public class ApiResponse {
    private String status;
    private String message;

    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
