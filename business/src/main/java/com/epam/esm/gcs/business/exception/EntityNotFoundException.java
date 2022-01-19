package com.epam.esm.gcs.business.exception;

public class EntityNotFoundException extends RuntimeException {

    private final static String ENTITY_NOT_FOUND_MCG = "Entity not found (%s where %s = %s)";

    private final static long serialVersionUID = -2334075934552684682L;

    public EntityNotFoundException() {
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String entityName, String searchParamName, Object searchParamValue) {
        super(String.format(ENTITY_NOT_FOUND_MCG, entityName, searchParamName, searchParamValue.toString()));
    }
}
