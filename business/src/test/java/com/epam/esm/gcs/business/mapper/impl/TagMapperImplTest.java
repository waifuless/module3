package com.epam.esm.gcs.business.mapper.impl;

import com.epam.esm.gcs.business.config.ModelMapperConfig;
import com.epam.esm.gcs.business.dto.TagDto;
import com.epam.esm.gcs.business.mapper.TagMapper;
import com.epam.esm.gcs.persistence.model.TagModel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TagMapperImplTest {

    private final TagMapper tagMapper = new TagMapperImpl(new ModelMapperConfig().modelMapper());

    @Test
    void toModel_returnModelEquivalentToDto() {
        Long id = 123L;
        String name = "name";
        TagDto tagDto = new TagDto(id, name);
        assertEquals(new TagModel(id, name), tagMapper.toModel(tagDto));
    }

    @Test
    void toDto_returnDtoEquivalentToModel() {
        Long id = 123L;
        String name = "name";
        TagModel tagModel = new TagModel(id, name);
        assertEquals(new TagDto(id, name), tagMapper.toDto(tagModel));
    }

    @Test
    void toModel_returnListOfModelsEquivalentListOfDtos() {
        List<TagModel> tagModels = new ArrayList<>();
        tagModels.add(new TagModel(1L, "1"));
        tagModels.add(new TagModel(2L, "2"));
        tagModels.add(new TagModel(3L, "3"));

        List<TagDto> tagDtos = new ArrayList<>();
        tagDtos.add(new TagDto(1L, "1"));
        tagDtos.add(new TagDto(2L, "2"));
        tagDtos.add(new TagDto(3L, "3"));

        assertEquals(tagModels, tagMapper.toModel(tagDtos));
    }

    @Test
    void toDto_returnListOfDtosEquivalentListOfModels() {
        List<TagModel> tagModels = new ArrayList<>();
        tagModels.add(new TagModel(1L, "1"));
        tagModels.add(new TagModel(2L, "2"));
        tagModels.add(new TagModel(3L, "3"));

        List<TagDto> tagDtos = new ArrayList<>();
        tagDtos.add(new TagDto(1L, "1"));
        tagDtos.add(new TagDto(2L, "2"));
        tagDtos.add(new TagDto(3L, "3"));

        assertEquals(tagDtos, tagMapper.toDto(tagModels));
    }
}