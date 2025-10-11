package com.ong.backend.exceptions;

public class DuplicadoException extends RuntimeException {
    public DuplicadoException(String message) {
        super(message);
    }
}