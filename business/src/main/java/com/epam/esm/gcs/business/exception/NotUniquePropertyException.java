package com.epam.esm.gcs.business.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotUniquePropertyException extends RuntimeException {

    private static final long serialVersionUID = 7939959298681285504L;

    private final Class<?> dtoClass;
    private final String field;
    private final String value;
}
