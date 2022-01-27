package com.epam.esm.gcs.business.service.impl;

import com.epam.esm.gcs.business.dto.TagDto;
import com.epam.esm.gcs.business.exception.EntityNotFoundException;
import com.epam.esm.gcs.business.exception.NotUniquePropertyException;
import com.epam.esm.gcs.business.service.TagService;
import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final static String ID_FIELD = "id";
    private final static String NAME_FIELD = "name";

    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    @Override
    public TagDto findById(Long id) {
        TagModel tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TagDto.class, ID_FIELD, String.valueOf(id)));
        return modelMapper.map(tag, TagDto.class);
    }

    @Override
    public List<TagDto> findAll() {
        return tagRepository.findAll().stream()
                .map(model -> modelMapper.map(model, TagDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TagDto create(TagDto tagDto) {
        if (existsByName(tagDto.getName())) {
            throw new NotUniquePropertyException(TagDto.class, NAME_FIELD, tagDto.getName());
        }
        TagModel tagModel = modelMapper.map(tagDto, TagModel.class);
        tagModel = tagRepository.create(tagModel);
        return modelMapper.map(tagModel, TagDto.class);
    }

    @Override
    public void delete(Long id) {
        tagRepository.delete(id);
    }

    @Override
    public boolean existsByName(String name) {
        return tagRepository.existsByName(name);
    }
}
