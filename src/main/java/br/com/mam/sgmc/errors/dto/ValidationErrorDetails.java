package br.com.mam.sgmc.errors.dto;

import java.time.Instant;
import java.util.List;

public class ValidationErrorDetails {
    private final Instant timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final List<FieldErrorDetails> details;

    public ValidationErrorDetails(Instant timestamp, int status, String error, String message,
            List<FieldErrorDetails> details) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.details = details;
    }

    public Instant getTimestamp() { return timestamp; }
    public int getStatus() { return status;}
    public String getError() { return error; }
    public String getMessage() { return message; }
    public List<FieldErrorDetails> getDetails() { return details; }
}
