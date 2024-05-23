package com.radiantdelta.bankapp.exceptions;

public class NotEnougnAmountException extends RuntimeException {
    public NotEnougnAmountException() {
        super();
    }

    public NotEnougnAmountException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnougnAmountException(String message) {
        super(message);
    }

    public NotEnougnAmountException(Throwable cause) {
        super(cause);
    }
}
