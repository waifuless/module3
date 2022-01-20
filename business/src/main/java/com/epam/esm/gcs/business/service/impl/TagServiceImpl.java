package com.epam.esm.gcs.business.service.impl;

import com.epam.esm.gcs.business.dto.TagDto;
import com.epam.esm.gcs.business.exception.EntityNotFoundException;
import com.epam.esm.gcs.business.exception.NotUniquePropertyException;
import com.epam.esm.gcs.business.service.TagService;
import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TagServiceImpl implements TagService {

    private final static String ENTITY_NAME = "tag";
    private final static String ID_FIELD = "id";
    private final static String NAME_FIELD = "name";

    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    @Override
    public TagDto findById(Long id) {
        return modelMapper.map(tagRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException(TagDto.class, ID_FIELD, String.valueOf(id))),
                TagDto.class);
    }

    @Override
    public List<TagDto> findAll() {
        return tagRepository.findAll().stream()
                .map(model -> modelMapper.map(model, TagDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TagDto create(TagDto tag) {
        if (existsByName(tag.getName())) {
            throw new NotUniquePropertyException(TagDto.class, NAME_FIELD, String.valueOf(tag.getName()));
        }
        return modelMapper.map(tagRepository.create(modelMapper.map(tag, TagModel.class)), TagDto.class);
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
