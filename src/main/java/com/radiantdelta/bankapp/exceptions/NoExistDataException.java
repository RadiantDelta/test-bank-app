package com.radiantdelta.bankapp.exceptions;

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
