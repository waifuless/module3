package com.epam.esm.gcs.business.validation;

import com.epam.esm.gcs.business.dto.TagDto;

public interface TagValidator {

    void validateForCreation(TagDto dto);

    void validateName(String name);
}
