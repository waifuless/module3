package com.epam.esm.gcs.business.service;

import com.epam.esm.gcs.business.dto.TagDto;

import java.util.List;

public interface TagService {

    TagDto findById(Long id);

    List<TagDto> findAll();

    TagDto create(TagDto tag);

    TagDto update(TagDto tag);

    TagDto remove(Long id);
}
