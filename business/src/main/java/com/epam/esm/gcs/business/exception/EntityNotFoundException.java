package com.epam.esm.gcs.business.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EntityNotFoundException extends RuntimeException {

    private final static long serialVersionUID = -7829145542271262719L;

    private final Class<?> dtoClass;
    private final String field;
    private final String value;
}
