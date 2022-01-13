package com.epam.esm.gcs.persistence.repository;

import com.epam.esm.gcs.persistence.exception.RepositoryException;

public interface UpdateRepository<T> {

    void update(T model) throws RepositoryException;
}
