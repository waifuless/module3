package com.epam.esm.gcs.business.service;

public interface CrudService<T> extends CrdService<T> {

    void updateById(Long id, T dto);
}
