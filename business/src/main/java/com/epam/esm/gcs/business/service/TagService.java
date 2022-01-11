package com.epam.esm.gcs.business.service;

import com.epam.esm.gcs.business.dto.TagDto;

import java.util.List;

public interface TagService {

    TagDto findById(Long id);

    List<TagDto> findAll();

    Long create(TagDto tag);

    void update(TagDto tag);

    void remove(Long id);
}
