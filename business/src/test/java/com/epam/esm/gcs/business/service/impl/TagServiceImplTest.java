package com.epam.esm.gcs.business.service.impl;

import com.epam.esm.gcs.business.dto.TagDto;
import com.epam.esm.gcs.business.exception.EntityNotFoundException;
import com.epam.esm.gcs.business.exception.NotUniquePropertyException;
import com.epam.esm.gcs.business.service.AppUserService;
import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    private final TagServiceImpl tagService;

    private final TagRepository tagRepository;
    private final AppUserService appUserService;

    public TagServiceImplTest(@Mock TagRepository tagRepository, @Mock AppUserService appUserService) {
        this.tagService = new TagServiceImpl(tagRepository,
                new ModelMapperTestConfig().modelMapper(), appUserService);
        this.tagRepository = tagRepository;
        this.appUserService = appUserService;
    }

    @Test
    void findById_returnDto_ifModelFound() {
        Long id = 2L;
        String name = "simpleName";
        TagModel tagModel = new TagModel(id, name);
        TagDto tagDto = new TagDto(id, name);
        when(tagRepository.findById(id)).thenReturn(Optional.of(tagModel));
        assertEquals(tagDto, tagService.findById(id));
    }

    @Test
    void findById_throwException_ifModelNotFound() {
        long id = 2L;
        when(tagRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> tagService.findById(id));
    }

    @Test
    void findPage_returnDtos_ifModelsFound() {
        List<TagModel> tagModels = new ArrayList<>();
        tagModels.add(new TagModel(1L, "1"));
        tagModels.add(new TagModel(2L, "2"));
        tagModels.add(new TagModel(3L, "3"));

        List<TagDto> tagDtos = new ArrayList<>();
        tagDtos.add(new TagDto(1L, "1"));
        tagDtos.add(new TagDto(2L, "2"));
        tagDtos.add(new TagDto(3L, "3"));

        Integer page = 3;
        Integer size = 4;
        Pageable inputPage = PageRequest.of(page, size);

        when(tagRepository.findPage(inputPage)).thenReturn(new PageImpl<>(tagModels));

        assertEquals(tagDtos, tagService.findPage(inputPage));
    }

    @Test
    void findPage_returnEmptyList_ifModelsNotFound() {
        List<TagModel> tagModels = Collections.emptyList();
        List<TagDto> tagDtos = Collections.emptyList();

        Integer page = 3;
        Integer size = 4;
        Pageable inputPage = PageRequest.of(page, size);

        when(tagRepository.findPage(inputPage)).thenReturn(new PageImpl<>(tagModels));

        assertEquals(tagDtos, tagService.findPage(inputPage));
    }

    @Test
    void create_returnNewDto_andTagDtoWithThatNameNotExists() {
        TagDto inputTagDto = new TagDto(null, "123");
        TagModel inputTagModel = new TagModel(null, "123");
        TagModel answerTagModel = new TagModel(123L, "123");
        TagDto answerTagDto = new TagDto(123L, "123");
        when(tagRepository.existsByName(inputTagDto.getName())).thenReturn(false);
        when(tagRepository.create(inputTagModel)).thenReturn(answerTagModel);

        assertEquals(answerTagDto, tagService.create(inputTagDto));
    }

    @Test
    void create_throwTagAlreadyExists_andTagDtoWithThatNameAlreadyExists() {
        TagDto inputTagDto = new TagDto(null, "123");

        when(tagRepository.existsByName(inputTagDto.getName())).thenReturn(true);

        assertThrows(NotUniquePropertyException.class, () -> tagService.create(inputTagDto));
    }

    @Test
    void delete_invokeTagRepositoryDelete_oneTime() {
        long id = 234L;

        tagService.delete(id);
        verify(tagRepository, times(1)).delete(id);
    }

    @Test
    void existsByName_returnFalse_tagWithThatNameDoNotExists() {
        String validName = "someValidName";
        when(tagRepository.existsByName(validName)).thenReturn(false);
        assertFalse(tagService.existsByName(validName));
    }

    @Test
    void existsByName_returnTrue_tagWithThatNameAlreadyExists() {
        String validName = "someValidName";
        when(tagRepository.existsByName(validName)).thenReturn(true);
        assertTrue(tagService.existsByName(validName));
    }

    @Test
    void findOrCreate_invokeCreate_whenTagIsNotFound() {
        long id = 6L;
        String name = "name";

        TagModel tagPreparedForCreation = new TagModel(name);
        TagModel createdTag = new TagModel(id, name);
        TagDto expectedReturnedTag = new TagDto(id, name);

        when(tagRepository.findByName(name)).thenReturn(Optional.empty());
        when(tagRepository.create(tagPreparedForCreation)).thenReturn(createdTag);

        assertEquals(expectedReturnedTag, tagService.findOrCreate(new TagDto(null, name)));

        verify(tagRepository, times(1)).create(tagPreparedForCreation);
    }

    @Test
    void findOrCreate_notInvokeCreate_whenTagIsFound() {
        long id = 8L;
        String name = "name2";

        TagModel foundTag = new TagModel(id, name);
        TagDto expectedReturnedTag = new TagDto(id, name);

        when(tagRepository.findByName(name)).thenReturn(Optional.of(foundTag));

        assertEquals(expectedReturnedTag, tagService.findOrCreate(new TagDto(null, name)));

        verify(tagRepository, times(0)).create(any());
    }
}