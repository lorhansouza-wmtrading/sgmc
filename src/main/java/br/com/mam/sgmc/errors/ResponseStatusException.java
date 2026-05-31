package br.com.mam.sgmc.errors;

public class ResponseStatusException extends RuntimeException {
    public ResponseStatusException(String message){
        super(message);
    } 
}

