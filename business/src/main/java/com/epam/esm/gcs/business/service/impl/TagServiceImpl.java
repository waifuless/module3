package com.epam.esm.gcs.business.service.impl;

import com.epam.esm.gcs.business.dto.TagDto;
import com.epam.esm.gcs.business.service.TagService;
import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public TagDto findById(Long id) {
        return new TagDto(id, tagRepository.findById(id).getName());
    }

    @Override
    public List<TagDto> findAll() {
        Iterable<TagModel> tagModels = tagRepository.findAll();
        List<TagDto> tagDtos = new ArrayList<>();
        for (TagModel tagModel : tagModels) {
            tagDtos.add(new TagDto(tagModel.getId(), tagModel.getName()));
        }
        return tagDtos;
    }

    @Override
    public Long create(TagDto tag) {
        return tagRepository.save(new TagModel(tag.getName()));
    }

    @Override
    public void remove(Long id) {
        //todo: check existence in db
        tagRepository.delete(id);
    }
}
