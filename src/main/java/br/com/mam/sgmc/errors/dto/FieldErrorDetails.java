package br.com.mam.sgmc.errors.dto;

public class FieldErrorDetails {
    private final String field;
    private final String message;

    public FieldErrorDetails(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() { return field; }
    public String getMessage() { return message; }
}
