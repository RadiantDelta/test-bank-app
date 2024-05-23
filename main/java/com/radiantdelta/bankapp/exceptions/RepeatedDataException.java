package com.radiantdelta.bankapp.exceptions;

public class RepeatedDataException extends RuntimeException{
    public RepeatedDataException() {
        super();
    }

    public RepeatedDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepeatedDataException(String message) {
        super(message);
    }

    public RepeatedDataException(Throwable cause) {
        super(cause);
    }
}
