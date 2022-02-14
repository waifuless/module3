package com.epam.esm.gcs.business.service.impl;

import com.epam.esm.gcs.business.config.ModelMapperConfig;
import com.epam.esm.gcs.business.dto.GiftCertificateDto;
import com.epam.esm.gcs.business.dto.GiftCertificateDtoContext;
import com.epam.esm.gcs.business.dto.TagDto;
import com.epam.esm.gcs.business.exception.EntityNotFoundException;
import com.epam.esm.gcs.business.service.GiftCertificateService;
import com.epam.esm.gcs.business.service.TagService;
import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModelContext;
import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.repository.GiftCertificateRepository;
import com.epam.esm.gcs.persistence.tableproperty.GiftCertificateColumn;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    private final GiftCertificateService giftCertificateService;

    private final GiftCertificateRepository giftCertificateRepository;
    private final TagService tagService;

    public GiftCertificateServiceImplTest(@Mock TagService tagService,
                                          @Mock GiftCertificateRepository giftCertificateRepository) {
        this.giftCertificateService = new GiftCertificateServiceImpl(tagService, giftCertificateRepository,
                new ModelMapperConfig().modelMapper());
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagService = tagService;
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
        List<TagModel> tagModels = List.of(new TagModel(tagId, tagName));
        GiftCertificateModel foundModel = GiftCertificateModel.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .createDate(createDate)
                .lastUpdateDate(lastUpdateDate)
                .tags(tagModels)
                .build();
        List<TagDto> tagDtos = List.of(new TagDto(tagId, tagName));
        GiftCertificateDto expectedReturnedCertificate = GiftCertificateDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .createDate(createDate)
                .lastUpdateDate(lastUpdateDate)
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

        List<TagDto> inputTags = List.of(new TagDto(null, tagName));
        GiftCertificateDto inputCertificate = GiftCertificateDto.builder()
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .tags(inputTags)
                .build();

        Long tagId = 3L;
        List<TagModel> preparedTags = List.of(new TagModel(tagId, tagName));
        GiftCertificateModel preparedForCreateCertificate = GiftCertificateModel.builder()
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
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
                .tags(expectedReturnedTags)
                .build();

        when(tagService.findOrCreate(new TagDto(null, tagName))).thenReturn(new TagDto(tagId, tagName));
        when(giftCertificateRepository.create(preparedForCreateCertificate)).thenReturn(createdCertificate);
        assertEquals(expectedReturnedCertificate, giftCertificateService.create(inputCertificate));
    }

    @Test
    void delete_invokeGiftCertificateRepositoryDelete_oneTime() {
        long id = 2342L;
        doNothing().when(giftCertificateRepository).delete(id);

        giftCertificateService.delete(id);
        verify(giftCertificateRepository, times(1)).delete(id);
    }

    @Test
    void updateById_throwEntityNotFoundException_whenEntityWithIdDoesNotExist() {
        long id = 23L;
        when(giftCertificateRepository.existsById(id)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () ->
                giftCertificateService.updateById(id, GiftCertificateDto.builder().build()));
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

        GiftCertificateModel expectedGiftCertificate = GiftCertificateModel.builder()
                .description(description)
                .duration(duration)
                .tags(preparedTags)
                .build();

        long certificateId = 5L;

        when(giftCertificateRepository.existsById(certificateId)).thenReturn(true);

        when(tagService.findOrCreate(new TagDto(null, tagName1))).thenReturn(new TagDto(tagId1, tagName1));
        when(tagService.findOrCreate(new TagDto(null, tagName2))).thenReturn(new TagDto(tagId2, tagName2));

        giftCertificateService.updateById(certificateId, inputGiftCertificate);

        verify(giftCertificateRepository, times(1)).updateById(certificateId, expectedGiftCertificate);
    }

    @Test
    void findAllByContext_invokeRepositoryWithValidModelContext() {
        String tagName = "justTagName";
        String searchValue = "simpleSearchValue";
        List<String> sortByList = List.of("name.asc");
        GiftCertificateDtoContext inputContext = GiftCertificateDtoContext.builder()
                .tagName(tagName)
                .searchValue(searchValue)
                .sortBy(sortByList)
                .build();

        GiftCertificateModelContext expectedContext = GiftCertificateModelContext.builder()
                .tagName(tagName)
                .searchValue(searchValue)
                .sortBy(Map.of("name", SortDirection.ASC))
                .build();

        giftCertificateService.findAll(inputContext);

        verify(giftCertificateRepository, times(1)).findAll(expectedContext);
    }
}