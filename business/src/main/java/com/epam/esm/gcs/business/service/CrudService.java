package com.epam.esm.gcs.business.service;

import java.util.List;

public interface CrudService<T> {

    T findById(Long id);

    List<T> findAll();

    Long create(T tag);

    void update(T tag);

    void remove(Long id);
}
