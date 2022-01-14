package com.epam.esm.gcs.business.validation.impl;

import com.epam.esm.gcs.business.dto.TagDto;
import com.epam.esm.gcs.business.exception.TagInvalidException;
import com.epam.esm.gcs.business.validation.TagValidator;
import org.springframework.stereotype.Component;

@Component
public class TagValidatorImpl implements TagValidator {

    private final static int MAX_NAME_SIZE = 100;
    private final static int MINIMAL_ID_VALUE = 1;

    @Override
    public void validateForCreation(TagDto dto) {
        if (dto == null) {
            throw new TagInvalidException();
        }
        validateName(dto.getName());
        validateId(dto.getId());
    }

    @Override
    public void validateName(String name) {
        if (name == null || name.isBlank() || name.length() > MAX_NAME_SIZE) {
            throw new TagInvalidException();
        }
    }

    @Override
    public void validateId(Long id) {
        if (id == null || id < MINIMAL_ID_VALUE) {
            throw new TagInvalidException();
        }
    }
}
