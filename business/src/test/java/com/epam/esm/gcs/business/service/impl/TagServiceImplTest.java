package com.epam.esm.gcs.business.service.impl;

import com.epam.esm.gcs.business.dto.TagDto;
import com.epam.esm.gcs.business.exception.EntityNotFoundException;
import com.epam.esm.gcs.business.exception.TagAlreadyExistsException;
import com.epam.esm.gcs.business.exception.TagInvalidException;
import com.epam.esm.gcs.business.mapper.TagMapper;
import com.epam.esm.gcs.business.validation.TagValidator;
import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagRepository tagRepository;
    @Mock
    private TagValidator tagValidator;
    @Mock
    private TagMapper tagMapper;

    @Test
    void findById_returnDto_ifModelFound() {
        Long id = 2L;
        String name = "simpleName";
        TagModel tagModel = new TagModel(id, name);
        TagDto tagDto = new TagDto(id, name);
        when(tagRepository.findById(id)).thenReturn(Optional.of(tagModel));
        when(tagMapper.toDto(tagModel)).thenReturn(tagDto);
        assertEquals(tagDto, tagService.findById(id));
    }

    @Test
    void findById_throwException_ifModelNotFound() {
        long id = 2L;
        when(tagRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> tagService.findById(id));
    }

    @Test
    void findAll_returnDtos_ifModelsFound() {
        List<TagModel> tagModels = new ArrayList<>();
        tagModels.add(new TagModel(1L, "1"));
        tagModels.add(new TagModel(2L, "2"));
        tagModels.add(new TagModel(3L, "3"));

        List<TagDto> tagDtos = new ArrayList<>();
        tagDtos.add(new TagDto(1L, "1"));
        tagDtos.add(new TagDto(2L, "2"));
        tagDtos.add(new TagDto(3L, "3"));

        when(tagRepository.findAll()).thenReturn(tagModels);
        when(tagMapper.toDto(tagModels)).thenReturn(tagDtos);

        assertEquals(tagDtos, tagService.findAll());
    }

    @Test
    void findAll_returnEmptyList_ifModelsNotFound() {
        List<TagModel> tagModels = Collections.emptyList();
        List<TagDto> tagDtos = Collections.emptyList();

        when(tagRepository.findAll()).thenReturn(tagModels);
        when(tagMapper.toDto(tagModels)).thenReturn(tagDtos);

        assertEquals(tagDtos, tagService.findAll());
    }

    @Test
    void create_returnNewDto_ifInputTagValid_andThatTagDoNotExists() {
        TagDto inputTagDto = new TagDto(null, "123");
        TagModel inputTagModel = new TagModel(null, "123");
        TagModel answerTagModel = new TagModel(123L, "123");
        TagDto answerTagDto = new TagDto(123L, "123");
        doNothing().when(tagValidator).validateForCreation(inputTagDto);
        when(tagRepository.existsByName(inputTagDto.getName())).thenReturn(false);
        when(tagMapper.toModel(inputTagDto)).thenReturn(inputTagModel);
        when(tagRepository.save(inputTagModel)).thenReturn(answerTagModel);
        when(tagMapper.toDto(answerTagModel)).thenReturn(answerTagDto);

        assertEquals(answerTagDto, tagService.create(inputTagDto));
    }

    @Test
    void create_throwTagInvalidException_ifInputTagInvalid() {
        TagDto inputTagDto = new TagDto(null, "123");
        doThrow(TagInvalidException.class).when(tagValidator).validateForCreation(inputTagDto);

        assertThrows(TagInvalidException.class, () -> tagService.create(inputTagDto));
    }

    @Test
    void create_throwTagAlreadyExists_ifInputTagValid_andThatTagAlreadyExists() {
        TagDto inputTagDto = new TagDto(null, "123");

        doNothing().when(tagValidator).validateForCreation(inputTagDto);
        when(tagRepository.existsByName(inputTagDto.getName())).thenReturn(true);

        assertThrows(TagAlreadyExistsException.class, () -> tagService.create(inputTagDto));
    }

    @Test
    void remove_invokeTagRepositoryDelete_oneTime() {
        long id = 234L;
        doNothing().when(tagRepository).delete(id);

        tagService.remove(id);
        verify(tagRepository, times(1)).delete(id);
    }

    @Test
    void existsByName_returnFalse_ifNameIsValid_andDoNotExists() {
        String validName = "someValidName";
        doNothing().when(tagValidator).validateName(validName);
        when(tagRepository.existsByName(validName)).thenReturn(false);
        assertFalse(tagService.existsByName(validName));
    }

    @Test
    void existsByName_returnTrue_ifNameIsValid_andExists() {
        String validName = "someValidName";
        doNothing().when(tagValidator).validateName(validName);
        when(tagRepository.existsByName(validName)).thenReturn(true);
        assertTrue(tagService.existsByName(validName));
    }

    @Test
    void existsByName_throwTagInvalidException_ifNameIsInvalid() {
        String validName = "someInvalidName";
        doThrow(TagInvalidException.class).when(tagValidator).validateName(validName);
        assertThrows(TagInvalidException.class, () -> tagService.existsByName(validName));
    }
}