package com.epam.esm.gcs.persistence.repository;

/**
 * Basic interface for Repositories with CRUD methods (create, read, update, delete)
 *
 * @param <T> - type of Model
 */
public interface CrudRepository<T> extends CrdRepository<T> {

    /**
     * Updates not null @param model fields in Model found by @param id.
     *
     * @param id    - id of Model to update
     * @param model - contains value of fields to update. Fields that should NOT be updated are null
     */
    void updateById(long id, T model);
}
