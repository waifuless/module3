package com.epam.esm.gcs.business.service;

import com.epam.esm.gcs.business.exception.EntityNotFoundException;
import com.epam.esm.gcs.business.exception.NotUniquePropertyException;

/**
 * Basic interface for all Services with CRD methods (create, read, delete)
 *
 * @param <T> - type of Dto
 */
public interface CrdService<T> {

    /**
     * Finds Dto by id
     *
     * @param id - id Of Dto to find
     * @return found Dto
     * @throws EntityNotFoundException when Dto is not found by id
     */
    T findById(Long id);

    /**
     * Prepares Dto for creation (if it needs) and creates Dto.
     *
     * @param dto - Dto for creating
     * @return Created Dto.
     * @throws NotUniquePropertyException when attempting to create dto with already existed unique attribute value
     */
    T create(T dto);

    /**
     * Deletes Dto by id
     *
     * @param id - id of Dto to delete
     */
    void delete(Long id);
}
