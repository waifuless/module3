package com.epam.esm.gcs.business.exception;

public class NotUniquePropertyException extends RuntimeException {

    private final static String NOT_UNIQUE_PROPERTY_MCG =
            "Entity with such property already exists (%s with %s = %s)";

    private static final long serialVersionUID = -2193299383602759449L;

    public NotUniquePropertyException() {
    }

    public NotUniquePropertyException(String message) {
        super(message);
    }

    public NotUniquePropertyException(String entityName, String duplicateParamName, Object duplicateParamValue) {
        super(String.format(NOT_UNIQUE_PROPERTY_MCG,
                entityName, duplicateParamName, duplicateParamValue.toString()));
    }
}
