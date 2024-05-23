package com.m1guelsb.springauth.exceptions;

public class NoExistDataException extends RuntimeException {
    public NoExistDataException() {
        super();
    }

    public NoExistDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoExistDataException(String message) {
        super(message);
    }

    public NoExistDataException(Throwable cause) {
        super(cause);
    }
}
