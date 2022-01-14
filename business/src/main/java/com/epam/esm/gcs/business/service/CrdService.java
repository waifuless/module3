package com.epam.esm.gcs.business.service;

import java.util.List;

public interface CrdService<T> {

    T findById(Long id);

    List<T> findAll();

    T save(T dto);

    void delete(Long id);
}
