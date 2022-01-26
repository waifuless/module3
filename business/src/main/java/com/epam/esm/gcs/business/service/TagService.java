package com.epam.esm.gcs.business.service;

import com.epam.esm.gcs.business.dto.TagDto;

import java.util.List;

public interface TagService extends CrdService<TagDto> {

    List<TagDto> findAll();

    boolean existsByName(String name);

    TagDto findOrCreate(String name);
}
