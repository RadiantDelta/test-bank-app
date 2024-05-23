package com.m1guelsb.springauth.exceptions;

public class LastDataException extends RuntimeException{
    public LastDataException() {
        super();
    }

    public LastDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public LastDataException(String message) {
        super(message);
    }

    public LastDataException(Throwable cause) {
        super(cause);
    }
}
