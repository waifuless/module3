package com.epam.esm.gcs.persistence.repository;

public interface UpdateRepository<T> {

    void update(T model);
}
