package com.epam.esm.gcs.persistence.repository;

/**
 * Basic interface for Delete operation on repository
 */
public interface DeleteRepository {

    /**
     * Deletes Model by id from persistence
     *
     * @param id - id of Model to delete
     */
    void delete(long id);
}
