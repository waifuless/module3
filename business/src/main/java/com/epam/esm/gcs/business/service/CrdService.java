package com.epam.esm.gcs.business.service;

import com.epam.esm.gcs.business.exception.NotUniquePropertyException;

/**
 * Basic interface for all Services with CRD methods (create, read, delete)
 *
 * @param <T> - type of Dto
 */
public interface CrdService<T> extends ReadService<T> {

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
