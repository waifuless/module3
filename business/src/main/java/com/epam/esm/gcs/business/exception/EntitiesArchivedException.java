package com.epam.esm.gcs.business.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.modelmapper.internal.Pair;

import java.util.List;

@Getter
@AllArgsConstructor
public class EntitiesArchivedException extends RuntimeException {

    private static final long serialVersionUID = -8135216827790441856L;

    private final Class<?> dtoClass;
    private final List<Pair<Long, Long>> archivedToActual;
    private final List<Long> unavailable;
}
