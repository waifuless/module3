package com.epam.esm.gcs.persistence.repository;

import java.util.Optional;

public interface ReadRepository<T> {

    /**
     * Finds Optional.Model by id. (Optional.empty if model not found)
     *
     * @param id - id Of Model to find
     * @return found Optional.Model or Optional.empty if model not found
     */
    Optional<T> findById(long id);
}
