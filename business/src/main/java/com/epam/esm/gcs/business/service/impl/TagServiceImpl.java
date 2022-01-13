package com.epam.esm.gcs.business.service.impl;

import com.epam.esm.gcs.business.dto.TagDto;
import com.epam.esm.gcs.business.exception.EntityAlreadyExistsException;
import com.epam.esm.gcs.business.mapper.TagMapper;
import com.epam.esm.gcs.business.service.TagService;
import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    @Override
    public TagDto findById(Long id) {
        return tagMapper.toDto(tagRepository.findById(id));
    }

    @Override
    public List<TagDto> findAll() {
        List<TagModel> tagModels = tagRepository.findAll();
        return tagMapper.toDto(tagModels);
    }

    @Override
    public Long create(TagDto tag) {
        if (existsByName(tag.getName())) {
            throw new EntityAlreadyExistsException();
        }
        return tagRepository.save(tagMapper.toModel(tag));
    }

    @Override
    public void remove(Long id) {
        tagRepository.delete(id);
    }

    @Override
    public boolean existsByName(String name) {
        return tagRepository.existsByName(name);
    }
}
