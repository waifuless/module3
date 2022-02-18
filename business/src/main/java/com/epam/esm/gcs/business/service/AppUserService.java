package com.epam.esm.gcs.business.service;

import com.epam.esm.gcs.business.dto.AppUserDto;

import java.util.List;

public interface AppUserService extends ReadService<AppUserDto> {

    /**
     * Finds all AppUserDtos
     *
     * @return List of all AppUserDtos
     */
    List<AppUserDto> findAll();
}
