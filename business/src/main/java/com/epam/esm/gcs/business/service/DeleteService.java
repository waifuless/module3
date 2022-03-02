package com.epam.esm.gcs.business.service;

/**
 * Basic interface for Delete operation on service
 */
public interface DeleteService {

    /**
     * Deletes Dto by id
     *
     * @param id - id of Dto to delete
     */
    void delete(Long id);
}
