package com.epam.esm.gcs.business.service;

import com.epam.esm.gcs.business.dto.TagDto;

public interface TagService extends CrdService<TagDto> {

    boolean existsByName(String name);
}
