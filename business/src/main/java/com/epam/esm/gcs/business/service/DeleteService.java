package com.epam.esm.gcs.business.service;

public interface DeleteService {

    /**
     * Deletes Dto by id
     *
     * @param id - id of Dto to delete
     */
    void delete(Long id);
}
