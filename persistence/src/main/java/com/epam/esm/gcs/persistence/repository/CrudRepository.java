package com.epam.esm.gcs.persistence.repository;

import com.epam.esm.gcs.persistence.exception.RepositoryException;

import java.util.Optional;

public interface CrudRepository<T> {

    T save(T model) throws RepositoryException;

    Optional<T> findById(long id) throws RepositoryException;

    Iterable<T> findAll() throws RepositoryException;

    T update(T model) throws RepositoryException;

    void delete(long id) throws RepositoryException;
}
