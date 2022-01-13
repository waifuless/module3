package com.epam.esm.gcs.business.exception;

public class EntityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -401339724009700957L;

    public EntityNotFoundException() {
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }
}
