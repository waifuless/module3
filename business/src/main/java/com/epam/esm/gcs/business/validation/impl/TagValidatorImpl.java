package com.epam.esm.gcs.business.validation.impl;

import com.epam.esm.gcs.business.dto.TagDto;
import com.epam.esm.gcs.business.exception.TagInvalidException;
import com.epam.esm.gcs.business.validation.TagValidator;
import org.springframework.stereotype.Component;

@Component
public class TagValidatorImpl implements TagValidator {

    private final static int MAX_NAME_SIZE = 100;

    @Override
    public void validateForCreation(TagDto dto) {
        if (dto == null) {
            throw new TagInvalidException();
        }
        validateName(dto.getName());
    }

    @Override
    public void validateName(String name) {
        if (name == null || name.isBlank() || name.length() > MAX_NAME_SIZE) {
            throw new TagInvalidException();
        }
    }
}
