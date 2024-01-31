package com.runapp.shoesservice.exception;

public class NoEntityFoundException extends RuntimeException {
    public NoEntityFoundException(String message) {
        super(message);
    }
    public NoEntityFoundException() {
        super();
    }
}
