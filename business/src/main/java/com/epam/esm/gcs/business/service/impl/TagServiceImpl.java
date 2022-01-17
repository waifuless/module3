package com.epam.esm.gcs.business.service.impl;

import com.epam.esm.gcs.business.dto.TagDto;
import com.epam.esm.gcs.business.exception.EntityNotFoundException;
import com.epam.esm.gcs.business.exception.TagAlreadyExistsException;
import com.epam.esm.gcs.business.service.TagService;
import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.repository.TagRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, ModelMapper modelMapper) {
        this.tagRepository = tagRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TagDto findById(Long id) {
        return modelMapper.map(tagRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new), TagDto.class);
    }

    @Override
    public List<TagDto> findAll() {
        return tagRepository.findAll().stream()
                .map(model -> modelMapper.map(model, TagDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TagDto save(TagDto tag) {
        if (existsByName(tag.getName())) {
            throw new TagAlreadyExistsException();
        }
        return modelMapper.map(tagRepository.save(modelMapper.map(tag, TagModel.class)), TagDto.class);
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
