package com.epam.esm.gcs.business.service;

import com.epam.esm.gcs.business.exception.EntityNotFoundException;

/**
 * Basic interface for all Services with CRUD methods (create, read, update, delete)
 *
 * @param <T> - type of Dto
 */
public interface CrudService<T> extends CrdService<T> {

    /**
     * Updates not null @param dto fields in Dto found by @param id.
     *
     * @param id  - id of Dto to update
     * @param dto - contains value of fields to update. Fields that should NOT be updated are null
     * @throws EntityNotFoundException when Dto for updating is not found by id
     */
    void updateById(Long id, T dto);
}
