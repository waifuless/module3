package com.epam.esm.gcs.business.service;

import com.epam.esm.gcs.business.exception.EntityNotFoundException;

public interface ReadService<T> {

    /**
     * Finds Dto by id
     *
     * @param id - id Of Dto to find
     * @return found Dto
     * @throws EntityNotFoundException when Dto is not found by id
     */
    T findById(Long id);
}
