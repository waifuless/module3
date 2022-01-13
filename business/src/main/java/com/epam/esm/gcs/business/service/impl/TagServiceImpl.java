package com.epam.esm.gcs.business.service.impl;

import com.epam.esm.gcs.business.dto.TagDto;
import com.epam.esm.gcs.business.exception.EntityNotFoundException;
import com.epam.esm.gcs.business.exception.TagAlreadyExistsException;
import com.epam.esm.gcs.business.mapper.TagMapper;
import com.epam.esm.gcs.business.service.TagService;
import com.epam.esm.gcs.business.validation.TagValidator;
import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.repository.TagRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final TagValidator tagValidator;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, TagMapper tagMapper, TagValidator tagValidator) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
        this.tagValidator = tagValidator;
    }

    @Override
    public TagDto findById(@NonNull Long id) {
        return tagMapper.toDto(tagRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public List<TagDto> findAll() {
        List<TagModel> tagModels = tagRepository.findAll();
        return tagMapper.toDto(tagModels);
    }

    @Override
    public TagDto create(@NonNull TagDto tag) {
        tagValidator.validateForCreation(tag);
        if (existsByNameWithoutValidation(tag.getName())) {
            throw new TagAlreadyExistsException();
        }
        return tagMapper.toDto(tagRepository.save(tagMapper.toModel(tag)));
    }

    @Override
    public void remove(@NonNull Long id) {
        tagRepository.delete(id);
    }

    @Override
    public boolean existsByName(@NonNull String name) {
        tagValidator.validateName(name);
        return existsByNameWithoutValidation(name);
    }

    private boolean existsByNameWithoutValidation(String name) {
        return tagRepository.existsByName(name);
    }
}
