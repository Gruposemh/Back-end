package com.ong.backend.exceptions;

public class BlogNaoEncontradoException extends RuntimeException {
    public BlogNaoEncontradoException(String message) {
        super(message);
    }
}
