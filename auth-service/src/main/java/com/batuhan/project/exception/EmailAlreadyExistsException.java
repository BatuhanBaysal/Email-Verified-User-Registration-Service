package com.batuhan.project.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super("This email address is already in use: " + email);
    }
}