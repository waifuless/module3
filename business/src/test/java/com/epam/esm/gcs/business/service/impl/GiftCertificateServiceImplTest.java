package com.epam.esm.gcs.business.service.impl;

import com.epam.esm.gcs.business.dto.ActualityStateDto;
import com.epam.esm.gcs.business.dto.GiftCertificateDto;
import com.epam.esm.gcs.business.dto.GiftCertificateDtoContext;
import com.epam.esm.gcs.business.dto.TagDto;
import com.epam.esm.gcs.business.exception.EntityNotFoundException;
import com.epam.esm.gcs.business.service.GiftCertificateService;
import com.epam.esm.gcs.business.service.TagService;
import com.epam.esm.gcs.business.validation.GiftCertificateValidator;
import com.epam.esm.gcs.persistence.model.ActualityStateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModelContext;
import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.repository.GiftCertificateRepository;
import com.epam.esm.gcs.persistence.tableproperty.SortDirection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.epam.esm.gcs.business.converter.GiftCertificateContextDtoConverter.FieldNameAssociation.NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    private final GiftCertificateService giftCertificateService;

    private final GiftCertificateRepository giftCertificateRepository;
    private final TagService tagService;
    private final GiftCertificateValidator giftCertificateValidator;

    public GiftCertificateServiceImplTest(@Mock TagService tagService,
                                          @Mock GiftCertificateRepository giftCertificateRepository,
                                          @Mock GiftCertificateValidator giftCertificateValidator) {
        this.giftCertificateService = new GiftCertificateServiceImpl(tagService, giftCertificateRepository,
                new ModelMapperTestConfig().modelMapper(), giftCertificateValidator);
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagService = tagService;
        this.giftCertificateValidator = giftCertificateValidator;
    }

    @Test
    void findById_returnDto_ifModelFound() {
        Long id = 2L;
        String name = "name";
        String description = "description";
        BigDecimal price = BigDecimal.valueOf(5.2).setScale(2, RoundingMode.HALF_UP);
        int duration = 2;
        LocalDateTime createDate = LocalDateTime.of(2020, 7, 30, 12, 23, 23, 304000);
        LocalDateTime lastUpdateDate = LocalDateTime.of(2020, 8, 28, 11, 34, 34, 60900);
        Long tagId = 3L;
        String tagName = "tagName";

        ActualityStateDto stateDto = ActualityStateDto.ARCHIVED;
        ActualityStateModel stateModel = ActualityStateModel.ARCHIVED;
        Integer count = 33;
        Long successorId = 5L;
        GiftCertificateModel successor = GiftCertificateModel.builder()
                .id(successorId)
                .build();

        List<TagModel> tagModels = List.of(new TagModel(tagId, tagName));
        List<TagDto> tagDtos = List.of(new TagDto(tagId, tagName));

        GiftCertificateModel foundModel = GiftCertificateModel.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .createDate(createDate)
                .lastUpdateDate(lastUpdateDate)
                .state(stateModel)
                .count(count)
                .successor(successor)
                .tags(tagModels)
                .build();
        GiftCertificateDto expectedReturnedCertificate = GiftCertificateDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .createDate(createDate)
                .lastUpdateDate(lastUpdateDate)
                .state(stateDto)
                .count(count)
                .successorId(successorId)
                .tags(tagDtos)
                .build();

        when(giftCertificateRepository.findById(id)).thenReturn(Optional.of(foundModel));
        assertEquals(expectedReturnedCertificate, giftCertificateService.findById(id));
    }

    @Test
    void findById_throwException_ifModelNotFound() {
        long id = 2L;
        when(giftCertificateRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> giftCertificateService.findById(id));
    }

    @Test
    void create_returnDto_createdByRepository() {
        String name = "name";
        String description = "description";
        BigDecimal price = BigDecimal.valueOf(5.2).setScale(2, RoundingMode.HALF_UP);
        int duration = 2;
        String tagName = "tagName";

        ActualityStateDto stateDto = ActualityStateDto.ACTUAL;
        ActualityStateModel stateModel = ActualityStateModel.ACTUAL;
        Integer count = 33;
        Long successorId = null;
        GiftCertificateModel successor = null;

        List<TagDto> inputTags = List.of(new TagDto(null, tagName));
        GiftCertificateDto inputCertificate = GiftCertificateDto.builder()
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .state(stateDto)
                .count(count)
                .successorId(successorId)
                .tags(inputTags)
                .build();

        Long tagId = 3L;
        List<TagModel> preparedTags = List.of(new TagModel(tagId, tagName));
        GiftCertificateModel preparedForCreateCertificate = GiftCertificateModel.builder()
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .state(stateModel)
                .count(count)
                .successor(successor)
                .tags(preparedTags)
                .build();

        Long certificateId = 2L;
        LocalDateTime createDate = LocalDateTime.of(2020, 7, 30, 12, 23, 23);
        LocalDateTime lastUpdateDate = LocalDateTime.of(2020, 8, 28, 11, 34, 34);
        GiftCertificateModel createdCertificate = GiftCertificateModel.builder()
                .id(certificateId)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .createDate(createDate)
                .lastUpdateDate(lastUpdateDate)
                .state(stateModel)
                .count(count)
                .successor(successor)
                .tags(preparedTags)
                .build();

        List<TagDto> expectedReturnedTags = List.of(new TagDto(tagId, tagName));

        GiftCertificateDto expectedReturnedCertificate = GiftCertificateDto.builder()
                .id(certificateId)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .createDate(createDate)
                .lastUpdateDate(lastUpdateDate)
                .state(stateDto)
                .count(count)
                .successorId(successorId)
                .tags(expectedReturnedTags)
                .build();

        when(tagService.findOrCreate(new TagDto(null, tagName))).thenReturn(new TagDto(tagId, tagName));
        when(giftCertificateRepository.create(preparedForCreateCertificate)).thenReturn(createdCertificate);
        assertEquals(expectedReturnedCertificate, giftCertificateService.create(inputCertificate));
    }

    @Test
    void updateById_throwEntityNotFoundException_whenEntityWithIdDoesNotExist() {
        long id = 23L;
        when(giftCertificateRepository.existsById(id)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () ->
                giftCertificateService.archiveAndCreateSuccessor(id, GiftCertificateDto.builder().build()));
    }

    @Test
    void updateById_invokeGiftCertificateRepositoryUpdate_withPreparedGiftCertificate() {
        long tagId1 = 3L;
        String tagName1 = "name";
        long tagId2 = 4L;
        String tagName2 = "name2";
        List<TagDto> inputTags = List.of(new TagDto(null, tagName1), new TagDto(null, tagName2));

        String description = "description";
        int duration = 5;
        GiftCertificateDto inputGiftCertificate = GiftCertificateDto.builder()
                .description(description)
                .duration(duration)
                .tags(inputTags)
                .build();

        List<TagModel> preparedTags = List.of(new TagModel(tagId1, tagName1), new TagModel(tagId2, tagName2));
        List<TagDto> preparedTagsDto = List.of(new TagDto(tagId1, tagName1), new TagDto(tagId2, tagName2));

        GiftCertificateModel inputGiftCertificateModel = GiftCertificateModel.builder()
                .description(description)
                .duration(duration)
                .tags(preparedTags)
                .build();

        long certificateId = 5L;

        long sId = 6L;
        String sName = "i am successor";
        BigDecimal price = BigDecimal.valueOf(22.22);
        ActualityStateModel sStateModel = ActualityStateModel.ACTUAL;
        ActualityStateDto sStateDto = ActualityStateDto.ACTUAL;
        Integer count = 22;

        GiftCertificateModel successor = GiftCertificateModel.builder()
                .id(sId)
                .name(sName)
                .description(description)
                .price(price)
                .duration(duration)
                .state(sStateModel)
                .count(count)
                .successor(null)
                .tags(preparedTags)
                .build();

        GiftCertificateDto successorDto = GiftCertificateDto.builder()
                .id(sId)
                .name(sName)
                .description(description)
                .price(price)
                .duration(duration)
                .state(sStateDto)
                .count(count)
                .successorId(null)
                .tags(preparedTagsDto)
                .build();

        when(giftCertificateRepository.existsById(certificateId)).thenReturn(true);

        when(tagService.findOrCreate(new TagDto(null, tagName1))).thenReturn(new TagDto(tagId1, tagName1));
        when(tagService.findOrCreate(new TagDto(null, tagName2))).thenReturn(new TagDto(tagId2, tagName2));
        when(giftCertificateRepository.archiveAndCreateSuccessor(certificateId, inputGiftCertificateModel))
                .thenReturn(successor);

        assertEquals(successorDto, giftCertificateService.archiveAndCreateSuccessor(certificateId,
                inputGiftCertificate));
    }

    @Test
    void findAllByContext_invokeRepositoryWithValidModelContext() {
        String tagName = "justTagName";
        String searchValue = "simpleSearchValue";
        List<String> sortByList = List.of("name.asc");
        GiftCertificateDtoContext inputContext = GiftCertificateDtoContext.builder()
                .tagName(Set.of(tagName))
                .searchValue(searchValue)
                .sortBy(sortByList)
                .build();

        GiftCertificateModelContext expectedContext = GiftCertificateModelContext.builder()
                .tagNames(Set.of(tagName))
                .searchValue(searchValue)
                .sortDirectionByFieldNameMap(Map.of(NAME.getFieldName(), SortDirection.ASC))
                .build();

        giftCertificateService.findAll(inputContext);

        verify(giftCertificateRepository, times(1)).findAll(expectedContext);
    }
}