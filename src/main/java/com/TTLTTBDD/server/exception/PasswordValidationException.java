package com.TTLTTBDD.server.exception;

import java.util.List;

public class PasswordValidationException extends RuntimeException {
    private final List<String> errors;

    public PasswordValidationException(List<String> errors) {
        super("Password không hợp lệ");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
