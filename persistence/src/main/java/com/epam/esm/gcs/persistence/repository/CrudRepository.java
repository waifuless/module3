package com.epam.esm.gcs.persistence.repository;

public interface CrudRepository<T> extends CrdRepository<T> {

    void update(T model);
}
