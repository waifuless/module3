package com.epam.esm.gcs.persistence.repository;

import com.epam.esm.gcs.persistence.exception.RepositoryException;

import java.util.List;

public interface CrdRepository<T> {

    Long save(T model) throws RepositoryException;

    T findById(long id) throws RepositoryException;

    List<T> findAll() throws RepositoryException;

    void delete(long id) throws RepositoryException;

    Boolean existsById(long id) throws RepositoryException;
}
