package com.epam.esm.gcs.business.mapper.impl;

import com.epam.esm.gcs.business.dto.TagDto;
import com.epam.esm.gcs.business.mapper.TagMapper;
import com.epam.esm.gcs.persistence.model.TagModel;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TagMapperImpl implements TagMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public TagMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public TagModel toModel(@NonNull TagDto dto) {
        return modelMapper.map(dto, TagModel.class);
    }

    @Override
    public TagDto toDto(@NonNull TagModel model) {
        return modelMapper.map(model, TagDto.class);
    }
}
