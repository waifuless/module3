package com.epam.esm.gcs.persistence.repository;

import java.util.Optional;

public interface CrdRepository<T> {

    T create(T model);

    Optional<T> findById(long id);

    void delete(long id);

    Boolean existsById(long id);
}
