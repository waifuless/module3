package com.epam.esm.gcs.persistence.repository;

public interface CrudRepository<T> extends CrdRepository<T> {

    void updateById(long id, T model);
}
