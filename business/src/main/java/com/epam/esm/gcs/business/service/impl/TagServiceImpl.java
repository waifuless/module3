package com.epam.esm.gcs.business.service.impl;

import com.epam.esm.gcs.business.dto.TagDto;
import com.epam.esm.gcs.business.dto.UserWithMostlyUsedTagsDto;
import com.epam.esm.gcs.business.exception.NotUniquePropertyException;
import com.epam.esm.gcs.business.service.AppUserService;
import com.epam.esm.gcs.business.service.TagService;
import com.epam.esm.gcs.persistence.model.AppUserModel;
import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.repository.TagRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl extends AbstractReadService<TagDto, TagModel> implements TagService {

    private final static String NAME_FIELD = "name";

    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;
    private final AppUserService appUserService;

    public TagServiceImpl(TagRepository tagRepository, ModelMapper modelMapper, AppUserService appUserService) {
        super(tagRepository, modelMapper, TagDto.class);

        this.tagRepository = tagRepository;
        this.modelMapper = modelMapper;
        this.appUserService = appUserService;
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

    @Override
    public TagDto findOrCreate(TagDto tagToFind) {
        if (tagToFind.getId() != null) {
            return this.findById(tagToFind.getId());
        } else {
            TagModel resultTag = tagRepository
                    .findByName(tagToFind.getName())
                    .orElseGet(() -> tagRepository.create(new TagModel(tagToFind.getName())));
            return modelMapper.map(resultTag, TagDto.class);
        }
    }

    @Override
    public List<UserWithMostlyUsedTagsDto> findMostWidelyUsedTagsOfUsersWithHighestOrderPriceAmount() {
        List<AppUserModel> usersWithHighestOrderPriceAmount =
                appUserService.findUsersWithHighestPriceAmountOfAllOrders()
                        .stream()
                        .map(dto -> modelMapper.map(dto, AppUserModel.class))
                        .collect(Collectors.toList());

        if (usersWithHighestOrderPriceAmount.isEmpty()) {
            return Collections.emptyList();
        }

        return tagRepository.findMostWidelyUsedTagsOfUsersById(usersWithHighestOrderPriceAmount)
                .stream()
                .map(model -> modelMapper.map(model, UserWithMostlyUsedTagsDto.class))
                .collect(Collectors.toList());
    }
}
