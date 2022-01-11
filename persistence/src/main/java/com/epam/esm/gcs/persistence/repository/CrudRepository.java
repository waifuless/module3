package com.epam.esm.gcs.persistence.repository;

import com.epam.esm.gcs.persistence.exception.RepositoryException;

public interface CrudRepository<T> {

    Long save(T model) throws RepositoryException;

    T findById(long id) throws RepositoryException;

    Iterable<T> findAll() throws RepositoryException;

    void update(T model) throws RepositoryException;

    void delete(long id) throws RepositoryException;
}
