package com.epam.esm.gcs.business.service;

import lombok.NonNull;

import java.util.List;

public interface CrdService<T> {

    T findById(@NonNull Long id);

    List<T> findAll();

    T create(@NonNull T dto);

    void remove(@NonNull Long id);
}
