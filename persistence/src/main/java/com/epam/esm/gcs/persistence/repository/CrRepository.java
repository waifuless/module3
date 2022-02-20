package com.epam.esm.gcs.persistence.repository;

/**
 * Basic interface for Repositories with CRD methods (create, read, delete)
 *
 * @param <T> - type of Model
 */
public interface CrRepository<T> extends ReadRepository<T> {

    /**
     * Creates model
     *
     * @param model - Model prepared for creating
     * @return Created Model with autogenerated fields.
     */
    T create(T model);
}
