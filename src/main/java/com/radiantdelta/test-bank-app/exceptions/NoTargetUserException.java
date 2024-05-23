package com.m1guelsb.springauth.exceptions;

public class NoTargetUserException extends RuntimeException{
    public NoTargetUserException() {
        super();
    }

    public NoTargetUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoTargetUserException(String message) {
        super(message);
    }

    public NoTargetUserException(Throwable cause) {
        super(cause);
    }
}
