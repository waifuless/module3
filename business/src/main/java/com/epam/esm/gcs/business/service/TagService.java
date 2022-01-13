package com.epam.esm.gcs.business.service;

import com.epam.esm.gcs.business.dto.TagDto;
import lombok.NonNull;

public interface TagService extends CrdService<TagDto> {

    boolean existsByName(@NonNull String name);
}
