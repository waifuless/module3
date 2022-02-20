package com.epam.esm.gcs.persistence.repository;

public interface DeleteRepository {

    /**
     * Deletes Model by id from persistence
     *
     * @param id - id of Model to delete
     */
    void delete(long id);
}
