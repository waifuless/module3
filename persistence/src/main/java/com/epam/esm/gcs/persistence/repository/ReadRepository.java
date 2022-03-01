package com.epam.esm.gcs.persistence.repository;

import com.epam.esm.gcs.persistence.model.PageModel;
import com.epam.esm.gcs.persistence.model.PageParamsModel;

import java.util.Optional;

/**
 * Basic interface for Read operation on repository
 *
 * @param <T>
 */
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
    PageModel<T> findPage(PageParamsModel pageParams);

    /**
     * Finds count of some entity
     *
     * @return count of some entity
     */
    Long count();
}
