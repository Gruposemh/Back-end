package com.ong.backend.exceptions;

public class BlogDuplicadoException extends RuntimeException {
    public BlogDuplicadoException(String message) {
        super(message);
    }
}