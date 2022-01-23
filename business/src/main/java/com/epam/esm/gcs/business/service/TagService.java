package com.epam.esm.gcs.business.service;

import com.epam.esm.gcs.business.dto.TagDto;

import java.util.Optional;

public interface TagService extends CrdService<TagDto> {

    boolean existsByName(String name);

    Optional<TagDto> findByName(String name);
}
