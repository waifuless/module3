package com.epam.esm.gcs.business.service;

public interface CrdService<T> {

    T findById(Long id);

    T create(T dto);

    void delete(Long id);
}
