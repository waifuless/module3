package com.epam.esm.gcs.persistence.repository;

import com.epam.esm.gcs.persistence.model.PageModel;

import java.util.List;
import java.util.Optional;

public interface ReadRepository<T> {

    /**
     * Finds Optional.Model by id. (Optional.empty if model not found)
     *
     * @param id - id Of Model to find
     * @return found Optional.Model or Optional.empty if model not found
     */
    Optional<T> findById(long id);

    /**
     * Checks the existence of Model by id
     *
     * @param id - id to search
     * @return true - if Model with @param id exists, false - if Model with @param id does not exist
     */
    Boolean existsById(long id);

    /**
     * Finds models on page
     *
     * @return List of models on page
     */
    List<T> findPage(PageModel page);
}
