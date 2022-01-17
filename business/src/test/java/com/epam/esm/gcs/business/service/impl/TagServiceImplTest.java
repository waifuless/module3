package com.epam.esm.gcs.business.service.impl;

import com.epam.esm.gcs.business.dto.TagDto;
import com.epam.esm.gcs.business.exception.EntityNotFoundException;
import com.epam.esm.gcs.business.exception.NotUniquePropertyException;
import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/test-config.xml"})
class TagServiceImplTest {

    private final TagServiceImpl tagService;

    private final TagRepository tagRepository;

    public TagServiceImplTest(@Autowired ModelMapper modelMapper, @Mock TagRepository tagRepository) {
        this.tagService = new TagServiceImpl(tagRepository, modelMapper);
        this.tagRepository = tagRepository;
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

        assertEquals(tagDtos, tagService.findAll());
    }

    @Test
    void findAll_returnEmptyList_ifModelsNotFound() {
        List<TagModel> tagModels = Collections.emptyList();
        List<TagDto> tagDtos = Collections.emptyList();

        when(tagRepository.findAll()).thenReturn(tagModels);

        assertEquals(tagDtos, tagService.findAll());
    }

    @Test
    void save_returnNewDto_ifInputTagValid_andThatTagDoNotExists() {
        TagDto inputTagDto = new TagDto(null, "123");
        TagModel inputTagModel = new TagModel(null, "123");
        TagModel answerTagModel = new TagModel(123L, "123");
        TagDto answerTagDto = new TagDto(123L, "123");
        when(tagRepository.existsByName(inputTagDto.getName())).thenReturn(false);
        when(tagRepository.save(inputTagModel)).thenReturn(answerTagModel);

        assertEquals(answerTagDto, tagService.save(inputTagDto));
    }

    @Test
    void save_throwTagAlreadyExists_ifInputTagValid_andThatTagAlreadyExists() {
        TagDto inputTagDto = new TagDto(null, "123");

        when(tagRepository.existsByName(inputTagDto.getName())).thenReturn(true);

        assertThrows(NotUniquePropertyException.class, () -> tagService.save(inputTagDto));
    }

    @Test
    void delete_invokeTagRepositoryDelete_oneTime() {
        long id = 234L;
        doNothing().when(tagRepository).delete(id);

        tagService.delete(id);
        verify(tagRepository, times(1)).delete(id);
    }

    @Test
    void existsByName_returnFalse_ifNameIsValid_andDoNotExists() {
        String validName = "someValidName";
        when(tagRepository.existsByName(validName)).thenReturn(false);
        assertFalse(tagService.existsByName(validName));
    }

    @Test
    void existsByName_returnTrue_ifNameIsValid_andExists() {
        String validName = "someValidName";
        when(tagRepository.existsByName(validName)).thenReturn(true);
        assertTrue(tagService.existsByName(validName));
    }
}